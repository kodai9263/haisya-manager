package com.example.haisya_manager.controller;

import java.time.LocalDate;
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
import com.example.haisya_manager.entity.Driver;
import com.example.haisya_manager.entity.Member;
import com.example.haisya_manager.entity.Ride;
import com.example.haisya_manager.entity.RideChildEntry;
import com.example.haisya_manager.entity.RideEntry;
import com.example.haisya_manager.entity.RideMemberEntry;
import com.example.haisya_manager.form.DriverForm;
import com.example.haisya_manager.form.RideChildEntryForm;
import com.example.haisya_manager.form.RideEditForm;
import com.example.haisya_manager.form.RideMemberEntryForm;
import com.example.haisya_manager.form.RideRegisterForm;
import com.example.haisya_manager.security.UserDetailsImpl;
import com.example.haisya_manager.service.RideService;

@Controller
@RequestMapping("/admin/rides")
public class AdminRideController {
	private final RideService rideService;
	
	public AdminRideController(RideService rideService) {
		this.rideService = rideService;
	}
	// ログイン中ユーザーの配車一覧ページを表示する
	@GetMapping
	public String index(@RequestParam(name = "date", required = false) LocalDate date,
						@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						@PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.ASC) Pageable pageable,
						RedirectAttributes redirectAttributes,
						Model model) {
		Page<Ride> ridePage;
		Integer adminId = userDetailsImpl.getAdmin().getId();
	
		// 日付が検索された場合指定された日付のみの配車一覧を表示する
		if (date != null) {
			ridePage = rideService.findRidesByAdminIdAndDate(adminId, date, pageable);
		} else {
			ridePage = rideService.findRidesByAdminIdOrderByDateDesc(adminId, pageable);
		}
		
		// 指定された日付の配車が存在しない場合のエラー処理
		if (ridePage.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "指定された日の配車状況が存在しません。");
			return "redirect:/admin/rides";
		}
		
		model.addAttribute("date", date);
		model.addAttribute("ridePage", ridePage);
		
		return "admin/rides/index";
	}
	
	// 配車詳細を表示する
	@GetMapping("/{rideId}")
	public String show(@PathVariable(name = "rideId") Integer rideId,
					   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
					   RedirectAttributes redirectAttributes,
					   Model model)
	{
		Optional<Ride> optionalRide = rideService.findRideById(rideId);
		
		// 指定した運転手をリストで受け取り、ビューに渡す
		List<Driver> drivers = rideService.findDriversIdsByRideId(rideId);
		
		// 実際に配車乗車する保護者の名前をリストとして受け取り、ビューに渡す
		List<RideMemberEntry> rideMemberEntries = rideService.findMemberIdsByRideId(rideId);
		
		// 実際に配車される車に乗る子供の名前をリストとして受け取り、ビューに渡す
		List<RideChildEntry> rideChildEntries = rideService.findChildIdsByRideId(rideId);
		
		if (optionalRide.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "配車が存在しません。");
			return "redirect:/admin/rides";
		}
		
		Ride ride = optionalRide.get();
		
		// 登録したadmin_id以外の人が配車を見ようとした場合のエラー処理
		if (!ride.getAdmin().getId().equals(userDetailsImpl.getAdmin().getId())) {
			redirectAttributes.addFlashAttribute("errorMessage", "指定されたユーザーでログインしてください");
			return "redirect:/admin/rides";
		}
		
		model.addAttribute("drivers", drivers);
		model.addAttribute("rideChildEntries", rideChildEntries);
		model.addAttribute("rideMemberEntries", rideMemberEntries);
		model.addAttribute("ride", ride);
		
		return "admin/rides/show";
	}
	
	// 配車登録ページを表示する
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("rideRegisterForm", new RideRegisterForm());
		return "admin/rides/register";
	}
	
	// 配車を登録する
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated RideRegisterForm rideRegisterForm,
						 BindingResult bindingResult,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		if (bindingResult.hasErrors()) {
			model.addAttribute("rideRegisterForm", rideRegisterForm);
			return "admin/rides/register";
		}
		
		rideService.createRide(userDetailsImpl, rideRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "配車予定を登録しました。");
		return "redirect:/admin/rides";
	}
	
	// 配車編集ページを表示する
	@GetMapping("/{rideId}/edit")
	public String edit(@PathVariable(name = "rideId") Integer rideId,
					   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
					   RedirectAttributes redirectAttributes,
					   Model model)
	{
		Optional<Ride> optionalRide = rideService.findRideById(rideId);

		if (optionalRide.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "配車が存在しません。");
			return "redirect:/admin/rides";
		}
		
		// 配車可能な保護者をリストで取得し、ビューに渡す。
		List<RideEntry> rideMemberCanEntries = rideService.findMemberIdsByRideIdAndCanDriveTrue(rideId);
		
		// 実際に配車する運転手の名前をリストとして受け取り、ビューに渡す
		List<Driver> drivers = rideService.findDriversIdsByRideId(rideId);
		
		// 実際に配車に乗員する保護者の名前をリストとして受け取り、ビューに渡す
		List<RideMemberEntry> rideMemberEntries = rideService.findMemberIdsByRideId(rideId);
		
		// 実際に配車される車に乗る子供の名前をリストとして受け取り、ビューに渡す
		List<RideChildEntry> rideChildEntries = rideService.findChildIdsByRideId(rideId);
		
		Integer adminId = userDetailsImpl.getAdmin().getId();
		
		// admin_idに紐づく保護者をリストで取得する
		List<Member> memberList = rideService.findMemberIdsByAdminId(adminId);
		
		// admin_idに紐づく子供をリストで取得する
		List<Child> childrenList = rideService.findChildIdsByAdminId(adminId);
		
		Ride ride = optionalRide.get();
		
		RideEditForm rideEditForm = new RideEditForm();

	    // ドライバーを作成
	    List<DriverForm> driverForms = new ArrayList<>();
	   for (Driver driver : drivers) {
		   DriverForm driverForm = new DriverForm();
		   driverForm.setMemberName(driver.getMember().getName());
		   driverForms.add(driverForm);
	   }
	    while (driverForms.size() < 5) {
	    	driverForms.add(new DriverForm());
	    }
		
	    // 乗車する保護者を作成
	    List<RideMemberEntryForm> rideMemberEntryForms = new ArrayList<>();
	    for (RideMemberEntry rideMemberEntry : rideMemberEntries) {
	    	RideMemberEntryForm rideMemberEntryForm = new RideMemberEntryForm();
	    	rideMemberEntryForm.setMemberName(rideMemberEntry.getMember().getName());
	    	rideMemberEntryForms.add(rideMemberEntryForm);
	    }
	    while (rideMemberEntryForms.size() < 5) {
	    	rideMemberEntryForms.add(new RideMemberEntryForm());
	    }
	    
	   // 運転手ごとの子供の情報を作成
	    List<RideChildEntryForm> rideChildEntryForms = new ArrayList<>();
	    for (Driver driver : drivers) {
	    	RideChildEntryForm rideChildEntryForm = new RideChildEntryForm();
	    	rideChildEntryForm.setDriverName(driver.getMember().getName());
	    	List<Integer> childIds = new ArrayList<>();
	    	// この運転手に乗る子供のID
	    	for (RideChildEntry rideChildEntry : rideChildEntries) {
	    		if (rideChildEntry.getChild() != null) {
	    			// 運転手のIDと子供のIDを紐づける
	    			if (driver.getMember().getId().equals(rideChildEntry.getChild().getMember().getId())) {
	    				childIds.add(rideChildEntry.getChild().getId());
	    			}
	    		}
	    	}
	    	rideChildEntryForm.setChildIds(childIds);
	    	rideChildEntryForms.add(rideChildEntryForm);
	    }
	    while (rideChildEntryForms.size() < 5) {
	    	RideChildEntryForm emptyRideChildEntryForm = new RideChildEntryForm();
	    	emptyRideChildEntryForm.setChildIds(new ArrayList<>());
	    	rideChildEntryForms.add(emptyRideChildEntryForm);
	    }
	    
		rideEditForm.setDate(ride.getDate());
		rideEditForm.setDestination(ride.getDestination());
		rideEditForm.setMemo(ride.getMemo());
		rideEditForm.setDrivers(driverForms);
		rideEditForm.setRideMemberEntries(rideMemberEntryForms);
		rideEditForm.setRideChildEntries(rideChildEntryForms);
		
		model.addAttribute("ride", ride);
		model.addAttribute("rideMemberCanEntries", rideMemberCanEntries);
		model.addAttribute("memberList", memberList);
		model.addAttribute("rideChildEntries", rideChildEntries);
		model.addAttribute("childrenList", childrenList);
		model.addAttribute("rideEditForm", rideEditForm);
		
		return "admin/rides/edit";
	}
	
	// 配車を編集する
	@PostMapping("/{rideId}/update")
	public String update(@ModelAttribute @Validated RideEditForm rideEditForm,
						 BindingResult bindingResult,
						 @PathVariable(name = "rideId") Integer rideId,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 RedirectAttributes redirectAttributes,
						 Model model)
	{
		Optional<Ride> optionalRide = rideService.findRideById(rideId);
		
		if (optionalRide.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "配車が存在しません");
			return "redirect:/admin/rides";
		}
		Ride ride = optionalRide.get();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("ride", ride);
			model.addAttribute("rideEditForm", rideEditForm);
			return "admin/rides/edit";
		}
		rideService.createDriver(rideEditForm, ride);
		rideService.createRideMemberEntry(rideEditForm, ride);
		redirectAttributes.addFlashAttribute("successMessage", "配車予定を編集しました。");
		return "redirect:/admin/rides/{rideId}";
	}
	
	// 配車を削除する
	@PostMapping("/{rideId}/delete")
	public String delete(@PathVariable(name = "rideId") Integer rideId,
						 RedirectAttributes redirectAttributes)
	{
		Optional<Ride> optionalRide = rideService.findRideById(rideId);
		
		if (optionalRide.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "配車が存在しません");
			return "redirect:/admin/rides";
		}
		Ride ride = optionalRide.get();
		rideService.deleteRide(ride);
		redirectAttributes.addFlashAttribute("successMessage", "配車予定を削除しました。");
		return "redirect:/admin/rides";
	}
}
