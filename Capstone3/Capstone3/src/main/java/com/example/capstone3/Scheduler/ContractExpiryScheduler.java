package com.example.capstone3.Scheduler;

import com.example.capstone3.Model.FranchiseContracts;
import com.example.capstone3.Repository.FranchiseContractRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;




// Done by salem

@Component
@RequiredArgsConstructor
public class ContractExpiryScheduler {

    private final FranchiseContractRepository franchiseContractRepository;


    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void checkAllContractsForExpiry() {
        List<FranchiseContracts> contracts = franchiseContractRepository.findAll();
        for (FranchiseContracts contract : contracts) {
            contract.updateStatus();
            franchiseContractRepository.save(contract);
        }
        System.out.println("Checked all contracts for expiry.");
    }
}
