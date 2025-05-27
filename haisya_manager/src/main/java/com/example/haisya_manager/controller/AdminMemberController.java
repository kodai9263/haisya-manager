package com.example.haisya_manager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.haisya_manager.entity.Child;
import com.example.haisya_manager.entity.Member;
import com.example.haisya_manager.form.ChildEditForm;
import com.example.haisya_manager.form.ChildRegisterForm;
import com.example.haisya_manager.form.MemberEditForm;
import com.example.haisya_manager.form.MemberRegisterForm;
import com.example.haisya_manager.security.UserDetailsImpl;
import com.example.haisya_manager.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {
	private final MemberService memberService;
	
	// ログイン中ユーザーの保護者一覧ページを表示する
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
						@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
						RedirectAttributes redirectAttributes,
						Model model)
	{
		Page<Member> memberPage;
		Integer adminId = userDetailsImpl.getAdmin().getId();
		
		// 名前が検索された場合名前を表示する
		if (keyword != null) {
			memberPage = memberService.findMembersByAdminIdAndNameLike(adminId, keyword, pageable);
		} else {
			memberPage = memberService.findMembersByAdminId(adminId, pageable);
		}
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("memberPage", memberPage);
		return "admin/members/index";
	}
	
	// メンバー詳細を表示する
	@GetMapping("/{memberId}")
	public String show(@PathVariable(name = "memberId") Integer memberId,
					   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
					   RedirectAttributes redirectAttributes,
					   Model model)
	{
		Optional<Member> optionalMember = memberService.findMemberById(memberId);
		
		if (optionalMember.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "メンバーが存在しません。");
			return "redirect:/admin/members";
		}
		
		Member member = optionalMember.get();
		List<Child> childList = memberService.findChildrenByMemberId(memberId);
		
		// ログイン中以外の人がメンバー詳細を見た場合のエラー処理
		if (!member.getAdmin().getId().equals(userDetailsImpl.getAdmin().getId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "指定されたユーザーでログインしてください。");
			return "redirect:/admin/members";
		}
		
		model.addAttribute("childList", childList);
		model.addAttribute("member", member);
		
		return "admin/members/show";
	}
	
	// メンバー登録ページを表示する
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("memberRegisterForm", new MemberRegisterForm());
		return "admin/members/register";
	}

	// メンバーと子供を登録する
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated MemberRegisterForm memberRegisterForm,
						 BindingResult memberBindingResult,
						 @ModelAttribute @Validated ChildRegisterForm childRegisterForm,
						 BindingResult childBindingResult,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (memberBindingResult.hasErrors() || childBindingResult.hasErrors()) {
			model.addAttribute("memberRegisterForm", memberRegisterForm);
			return "admin/members/register";
		}
		
		memberService.createMember(userDetailsImpl, memberRegisterForm, childRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "メンバーを登録しました。");
		return "redirect:/admin/members";
	}
	
	// メンバーと子供の編集ページを表示する
	@GetMapping("/{memberId}/edit")
	public String edit(@PathVariable(name = "memberId") Integer memberId,
					   RedirectAttributes redirectAttributes,
					   Model model)
	{
		Optional<Member> optionalMember = memberService.findMemberById(memberId);
		
		if (optionalMember.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "メンバーが存在しません。");
			return "redirect:/admin/members";
		}
		
		Member member = optionalMember.get();
		MemberEditForm memberEditForm = new MemberEditForm();
		memberEditForm.setName(member.getName());
		
		// メンバーに紐づく子供の名前を取得する、編集フォームに表示させるため
		List<Child> children = memberService.findChildrenByMemberId(memberId);
		
		// 既存の子供の情報を編集フォームに変換する
		List<ChildEditForm> childEditFormList = new ArrayList<>();
		for (Child child : children) {
			ChildEditForm childEditForm = new ChildEditForm();
			// 取得した子供の名前を編集フォームを初期化して子供の名前をセットする
			childEditForm.setName(child.getName());
			childEditFormList.add(childEditForm);
		}
		// 3人未満の場合からの編集フォームを追加する
		while (childEditFormList.size() < 3) {
			childEditFormList.add(new ChildEditForm());
		}
		memberEditForm.setChildren(childEditFormList);
		
		model.addAttribute("member", member);
		model.addAttribute("memberEditForm", memberEditForm);
		return "admin/members/edit";
	}
	
	// メンバーを編集する
	@PostMapping("/{memberId}/update")
	public String update(@PathVariable(name = "memberId") Integer memberId,
						 @ModelAttribute @Validated MemberEditForm memberEditForm,
						 BindingResult memberBindingResult,
						 @ModelAttribute @Validated ChildEditForm childEditForm,
						 BindingResult childBindingResult,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		Optional<Member> optionalMember = memberService.findMemberById(memberId);
		
		if (optionalMember.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "メンバーが存在しません。");
			return "redirect:/admin/members";
		}
		
		if (memberBindingResult.hasErrors() || childBindingResult.hasErrors()) {
			model.addAttribute("memberEditForm", memberEditForm);
			return "admin/members/edit";
		}
		
		Member member = optionalMember.get();
		memberService.updateMember(userDetailsImpl, memberEditForm, childEditForm, member);
		redirectAttributes.addFlashAttribute("successMessage", "メンバーを編集しました。");
		return "redirect:/admin/members";
	}
	
	// メンバーを削除する
	@PostMapping("/{memberId}/delete")
	public String delete(@PathVariable(name = "memberId") Integer memberId,
						 RedirectAttributes redirectAttributes)
	{
		Optional<Member> optionalMember = memberService.findMemberById(memberId);
		
		if (optionalMember.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "メンバーが存在しません。");
			return "redirect:/admin/members";
		}
		
		Member member = optionalMember.get();
		memberService.deleteMember(member);
		redirectAttributes.addFlashAttribute("successMessage", "メンバーを削除しました。");
		return "redirect:/admin/members";
	}
}
