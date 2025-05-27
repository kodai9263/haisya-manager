package com.example.haisya_manager.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.haisya_manager.entity.Child;
import com.example.haisya_manager.entity.Driver;
import com.example.haisya_manager.entity.Member;
import com.example.haisya_manager.entity.Ride;
import com.example.haisya_manager.entity.RideChildEntry;
import com.example.haisya_manager.entity.RideEntry;
import com.example.haisya_manager.entity.RideMemberEntry;
import com.example.haisya_manager.form.DriverForm;
import com.example.haisya_manager.form.RideEditForm;
import com.example.haisya_manager.form.RideMemberEntryForm;
import com.example.haisya_manager.form.RideRegisterForm;
import com.example.haisya_manager.repository.ChildRepository;
import com.example.haisya_manager.repository.DriverRepository;
import com.example.haisya_manager.repository.MemberRepository;
import com.example.haisya_manager.repository.RideChildEntryRepository;
import com.example.haisya_manager.repository.RideEntryRepository;
import com.example.haisya_manager.repository.RideMemberEntryRepository;
import com.example.haisya_manager.repository.RideRepository;
import com.example.haisya_manager.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideService {
	private final RideRepository rideRepository;
	private final RideMemberEntryRepository rideMemberEntryRepository;
	private final RideChildEntryRepository rideChildEntryRepository;
	private final RideEntryRepository rideEntryRepository;
	private final MemberRepository memberRepository;
	private final ChildRepository childRepository;
	private final DriverRepository driverRepository;
	
	// 	ログイン中のadminIdに紐づく配車状況日付が新しい順にページングされた状態で取得する
	public Page<Ride> findRidesByAdminIdOrderByDateDesc(Integer adminId, Pageable pageable) {
		return rideRepository.findByAdminIdOrderByDateDesc(adminId, pageable);
	}
	
	// ログイン中のadmin_idが指定した日付を取得する
	public Page<Ride> findRidesByAdminIdAndDate(Integer adminId, LocalDate date, Pageable pageable) {
		return rideRepository.findByAdminIdAndDate(adminId, date, pageable);
	}
	
	// 指定したidを持つ配車を取得する
	public Optional<Ride> findRideById(Integer rideId) {
		return rideRepository.findById(rideId);
	}
	
	// 指定したride_idに紐づく乗員する保護者をリストで取得する
	public List<RideMemberEntry> findMemberIdsByRideId(Integer rideId) {
		return rideMemberEntryRepository.findByRideId(rideId);
	}
	
	// 指定したride_idに紐づく乗員する運転手をリストで取得する
	public List<Driver> findDriversIdsByRideId(Integer rideId) {
		return driverRepository.findByRideId(rideId);
	}
	
	// 指定したride_idに紐づく乗員する子供をリストで取得する
	public List<RideChildEntry> findChildIdsByRideId(Integer rideId) {
		return rideChildEntryRepository.findByRideId(rideId);
	}
	
	// 指定したride_idに紐づきtrueのみの保護者をリストで取得する
	public List<RideEntry> findMemberIdsByRideIdAndCanDriveTrue(Integer rideId) {
		return rideEntryRepository.findByRideIdAndCanDriveTrue(rideId);
	}
	
	// admin_idに紐づく保護者をリストで取得する
	public List<Member> findMemberIdsByAdminId(Integer adminId) {
		return memberRepository.findByAdminId(adminId);
	}
	
	// member_idに紐づく子供をリストで取得する
	public List<Child> findChildIdsByAdminId(Integer adminId) {
		return childRepository.findByAdminId(adminId);	
	}
	
	// 配車日、行き先、メモを登録する
	@Transactional
	public void createRide(UserDetailsImpl userDetailsImpl,
						   RideRegisterForm rideRegisterForm) {
		Ride ride = new Ride();
		
		ride.setDate(rideRegisterForm.getDate());
		ride.setDestination(rideRegisterForm.getDestination());
		ride.setMemo(rideRegisterForm.getMemo());
		ride.setAdmin(userDetailsImpl.getAdmin());
		
		rideRepository.save(ride);
	}
	
	// 配車登録した内容を更新する
	@Transactional
	public void updateRide(RideEditForm rideEditForm, Ride ride) {
		
		// ride.setDate(rideEditForm.getDate());
		ride.setDestination(rideEditForm.getDestination());
		ride.setMemo(rideEditForm.getMemo());
		
		rideRepository.save(ride);
	}
	
	// 配車号（運転手）を登録する
	@Transactional
	public void createDriver(RideEditForm rideEditForm, Ride ride) {
		driverRepository.deleteByRide(ride);
		driverRepository.flush();
		// RideEditFormで入力された運転手をループする
		for (DriverForm driverForm : rideEditForm.getDrivers()) {
			// 空欄ならスキップ
			if (driverForm.getMemberName() == null || driverForm.getMemberName().isBlank())
				continue;
			
			Member member = memberRepository.findByName(driverForm.getMemberName());
			if (member == null) {
				continue;
			}
			// Driverエンティティに登録していく
			Driver driver = new Driver();
			driver.setRide(ride);
			driver.setMember(member);
			driverRepository.save(driver);
		}
	}
	
	// 配車号（運転手）を登録する
		@Transactional
		public void createRideMemberEntry(RideEditForm rideEditForm, Ride ride) {
			rideMemberEntryRepository.deleteByRide(ride);
			rideMemberEntryRepository.flush();
			// RideEditFormで入力された運転手をループする
			for (RideMemberEntryForm rideMemberEntryForm : rideEditForm.getRideMemberEntries()) {
				// 空欄ならスキップ
				if (rideMemberEntryForm.getMemberName() == null || rideMemberEntryForm.getMemberName().isBlank())
					continue;
				
				Member member = memberRepository.findByName(rideMemberEntryForm.getMemberName());
				if (member == null) {
					continue;
				}
				// Driverエンティティに登録していく
				RideMemberEntry rideMemberEntry = new RideMemberEntry();
				rideMemberEntry.setRide(ride);
				rideMemberEntry.setMember(member);
				rideMemberEntryRepository.save(rideMemberEntry);
			}
		}
		
	/* 配車される車に乗車する保護者を登録する
	@Transactional
	public void createRideChild(RideEditForm rideEditForm, Ride ride) {
		// RideChildFormで入力された子供の数だけのループさせる
		for (RideChildEntryForm rideChildForm : rideEditForm.getChildEntries()) {
			// 配車に指定された保護者を取得する
			RideMemberEntry rideMemberEntry = rideMemberEntryRepository.findById(rideChildForm.getRideMemberEntryId()).orElseThrow(() -> new RuntimeException("Entry not found"));
			// 配車に指定された人に乗車する子供をループで取得する
			for (Integer childId : rideChildForm.getChildIds()) {
				RideChildEntry rideChildEntry = new RideChildEntry();
				rideChildEntry.setRide(ride);
				rideChildEntry.setRideMemberEntry(rideMemberEntry);
				Child child = childRepository.findById(childId).orElseThrow(() -> new RuntimeException("Child not found"));
				rideChildEntry.setChild(child);
				rideChildEntryRepository.save(rideChildEntry);
				
			}
		}
	}*/
	
	/* 配車号、乗車する子供を一掃してから編集登録する
	@Transactional
	public void updateAllRide(RideEditForm rideEditForm, Ride ride) {
		ride.setDate(rideEditForm.getDate());
		ride.setDestination(rideEditForm.getDestination());
		ride.setMemo(rideEditForm.getMemo());
		rideRepository.save(ride);
		
		Integer rideId = ride.getId();
		if (rideId == null) {
			throw new IllegalArgumentException("Ride ID cannot be null");
		}
		
		// 既存の子エントリ→メンバーエントリの順で一掃
		rideChildEntryRepository.deleteByRideId(ride.getId());
		// rideChildEntryRepository.flush();
		rideMemberEntryRepository.deleteByRideId(ride.getId()); 
		// rideMemberEntryRepository.flush();
		
		// 新しいエントリの作成
		List<RideMemberEntry> newMemberEntries = new ArrayList<>();
		
		for (DriverForm entryForm : rideEditForm.getRideMemberEntries()) {
			if (entryForm.getMemberName() == null || entryForm.getMemberName().isBlank()) continue;
			
			Member member = memberRepository.findByName(entryForm.getMemberName());
			if (member == null) {
				member = new Member();
				member.setName(entryForm.getMemberName());
				memberRepository.save(member);
			}
			
			RideMemberEntry rideMemberEntry = new RideMemberEntry();
			rideMemberEntry.setRide(ride);
			rideMemberEntry.setMember(member);
			rideMemberEntryRepository.save(rideMemberEntry);
			
			for (Integer childId : entryForm.getChildIds()) {
				childRepository.findById(childId).ifPresent(child -> {
					
					System.out.println(childId + "はあります。");
					
					RideChildEntry rideChildEntry = new RideChildEntry();
					rideChildEntry.setRide(ride);
					rideChildEntry.setRideMemberEntry(rideMemberEntry);
					rideChildEntry.setChild(child);
					rideChildEntryRepository.save(rideChildEntry);
				});
			}
			newMemberEntries.add(rideMemberEntry);
		}
	}*/
	
	// 配車を削除する
	@Transactional
	public void deleteRide(Ride ride) {
		rideRepository.delete(ride);
	}

}
