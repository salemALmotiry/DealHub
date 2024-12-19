package com.example.capstone3.Service;


import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.FranchiseOfferIn;
import com.example.capstone3.Model.*;
import com.example.capstone3.OutDTO.FranchiseOfferDTO;
import com.example.capstone3.OutDTO.OfferedBy;
import com.example.capstone3.Repository.FranchiseOfferRepository;
import com.example.capstone3.Repository.FranchiseRepository;
import com.example.capstone3.Repository.IndividualRepository;
import com.example.capstone3.Repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
// Done by salem
public class IndividualService {

    private final IndividualRepository individualRepository;
    private final FranchiseRepository franchiseRepository;
    private final FranchiseOfferRepository franchiseOfferRepository;
    private final MessageRepository messageRepository;


    public void addIndividual(Individual individual) {
       individualRepository.save(individual);
    }


    public void updateIndividual(Integer id,Individual individual) {
            Individual oldIndividual = individualRepository.findIndividualById(id);
            if(oldIndividual == null) {
                throw new ApiException("individual not found");

            }
            oldIndividual.setEmail(individual.getEmail());
            oldIndividual.setFullName(individual.getFullName());
            oldIndividual.setPhoneNumber(individual.getPhoneNumber());
            individualRepository.save(oldIndividual);
    }







}

