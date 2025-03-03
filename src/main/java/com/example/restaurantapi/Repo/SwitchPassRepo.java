package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Pass.SwitchPass;
import com.example.restaurantapi.Models.Pass.PassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwitchPassRepo extends JpaRepository<SwitchPass, Long> {
    List<SwitchPass> findByPassStatus(PassStatus passStatus);
}
