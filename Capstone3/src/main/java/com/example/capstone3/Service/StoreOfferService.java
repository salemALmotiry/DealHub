package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.StoreCounterOfferDTOIn;
import com.example.capstone3.Model.*;
import com.example.capstone3.OutDTO.StoreCounterOfferDTO;
import com.example.capstone3.Repository.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  StoreOfferService {
    private final StoreOfferRepository storeOfferRepository;
    private final StoreRepository storeRepository;
    private final IndividualRepository individualRepository;
    private final OwnerRepository ownerRepository;
    private final MessageRepository messageRepository;
    private final StoreContractRepository storeContractRepository;

    // get all offer for specific store  - by basil
    public List<StoreCounterOfferDTO> getStoreOfferByStoreId(Integer storeId){

        List<StoreOffer> storeOffers = storeOfferRepository.findStoreOfferByStoreId(storeId);
        if(storeOffers.isEmpty()) return new ArrayList<>();

        return storeOffers.stream().map(this::convertToStoreCounterOfferDTO).collect(Collectors.toList());
    }

    // get all offer pending by individual - by basil
    public List<StoreCounterOfferDTO> getStoreOfferPendingByIndividualId(Integer individualId){
        List<StoreOffer> storeOffers = storeOfferRepository.findStoreOfferByIndividualIdAndStatus(individualId,"Pending by individual");
        if(storeOffers.isEmpty()) return new ArrayList<>();
        return storeOffers.stream().map(this::convertToStoreCounterOfferDTO).collect(Collectors.toList());
    }
    // get all offer pending by owner - by basil
    public List<StoreCounterOfferDTO> getStoreOfferPendingByOwnerId(Integer ownerId){
        List<StoreOffer> storeOffers = storeOfferRepository.findAll().stream().toList().stream().filter(storeOffer -> storeOffer.getStatus().equalsIgnoreCase("Pending by owner") && storeOffer.getStore().getOwner().getId().equals(ownerId)).toList();
        if(storeOffers.isEmpty()) return new ArrayList<>();
        return storeOffers.stream().map(this::convertToStoreCounterOfferDTO).collect(Collectors.toList());
    }


    // get all by made by individual for specific store - by basil
    public List<StoreCounterOfferDTO> getOfferByIndividualIdAndStoreId(Integer individualId , Integer storeId){
        List<StoreOffer> storeOffers = storeOfferRepository.findStoreOfferByIndividualIdAndStoreId(individualId,storeId);
        if(storeOffers.isEmpty()) return new ArrayList<>();
        return storeOffers.stream().map(this::convertToStoreCounterOfferDTO).collect(Collectors.toList());
    }

    // Make offer with same deal - by basil
    public void makeOfferSameDeal(Integer individualId , Integer storeId  ){
        StoreOffer storeOffer =new StoreOffer();
        // check there is offer with same user active in store - by basil
        if (storeOfferRepository.getStoreOfferActive(individualId, storeId) != null) throw new ApiException("Error: Duplicated there is store offer active by the user");

        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("Error: Individual Not Found");
        Store store = storeRepository.findStoreById(storeId);
        if (store == null) throw new ApiException("Error: Store Offer Not Found");

        // Avoiding individual make offer when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(storeId) != null ) throw new ApiException("Error: store is sold you can't make offer ");

        // Avoiding individual make offer when store is inactive - by basil
        if (store.getStatus().equalsIgnoreCase("Inactive")) throw new ApiException("Error : store is Inactive you can't make offer " );

        // creating the offer with same deal - by basil
        storeOffer.setIndividual(individual);
        storeOffer.setStore(store);
        storeOffer.setOfferPrice(store.getOriginalPrice());
        storeOffer.setSameDeal(true);
        storeOffer.setStatus("Pending by owner");
        storeOfferRepository.save(storeOffer);
    }

    // make offer with counter the original deal - by basil
    public void makeOfferWithCounter(Integer individualId , Integer storeId , StoreCounterOfferDTOIn storeCounterOfferDTOIn ){
        StoreOffer storeOffer =new StoreOffer();
        // check there is offer with same user active in store - by basil
        if (storeOfferRepository.getStoreOfferActive(individualId, storeId) != null) throw new ApiException("Error: Duplicated there is store offer active by the user");

        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("Error: Individual Not Found");
        Store store = storeRepository.findStoreById(storeId);
        if (store == null) throw new ApiException("Error: Store Offer Not Found");
        // Avoiding individual make offer when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(storeId) != null ) throw new ApiException("Error: store is sold you can't make offer ");


        // Avoiding individual make offer when store is inactive - by basil
        if (store.getStatus().equalsIgnoreCase("Inactive")) throw new ApiException("Error : store is Inactive you can't make offer " );
        // creating the offer with counter - by basil
        storeOffer.setIndividual(individual);
        storeOffer.setStore(store);
        storeOffer.setOfferPrice(storeCounterOfferDTOIn.getCounterPrice());
        storeOffer.setSameDeal(false);
        storeOffer.setStatus("Pending by owner");
        storeOffer = storeOfferRepository.save(storeOffer);

        // Create Message with note from the individual to owner for Negotiation - by basil
        Message message  = new Message();
        message.setStoreOffer(storeOffer);
        message.setIndividual(individual);
        message.setMessage(storeCounterOfferDTOIn.getMessage());
        messageRepository.save(message);
    }


    // Owner counter back the deal to individual - by basil
    public void ownerCounterOffer( Integer offerId ,Integer ownerId, StoreCounterOfferDTOIn storeCounterOfferDTOIn ){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(offerId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");

        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("Error: Owner Not Found");

        Store store = storeOffer.getStore();
        // Avoiding Owner make Counter Offer when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(store.getId()) != null ) throw new ApiException("Error: store is sold you can't make Counter Offer ");

        // Avoiding owner updating offer not belong to him - by basil
        if (!Objects.equals(storeOffer.getStore().getOwner().getId(), ownerId)) throw new ApiException("Error: this store not belong to the owner");


        // Avoiding the owner make counter when the offer is with same deal - by basil
        if (storeOffer.getSameDeal()) throw new ApiException("Error: Store offer is with same deal you can't make counter offer.");

        // avoiding updating offer when is it closed - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected")) throw new ApiException("Error: this store offer is closed. the store offer status ether is approved or rejected ");

        // avoiding update when offer Pending by individual response - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Pending by individual")) throw new ApiException("Error: this store offer is Pending by the individual you can't make counter offer.");


        // avoiding owner make counter price greater than original Price - by basil
        Double originalPrice = storeOffer.getStore().getOriginalPrice();
        if (storeCounterOfferDTOIn.getCounterPrice() > originalPrice) throw new ApiException("Error: You can't make counter price greater than original Price.");

        storeOffer.setOfferPrice(storeCounterOfferDTOIn.getCounterPrice());
        storeOffer.setStatus("Pending by individual");
        storeOffer.setUpdatedAt(LocalDateTime.now());
        storeOfferRepository.save(storeOffer);

        // Create Message with note from the individual to owner for Negotiation - by basil
        Message message  = new Message();
        message.setStoreOffer(storeOffer);
        message.setOwner(owner);
        message.setMessage(storeCounterOfferDTOIn.getMessage());
        messageRepository.save(message);
    }

    // Owner approve the offer - by basil
    public void ownerApprove( Integer offerId ,Integer ownerId){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(offerId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");

        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("Error: Owner Not Found");

        Store store = storeOffer.getStore();
        // Avoiding Owner make approve when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(store.getId()) != null ) throw new ApiException("Error: store is sold you can't make approve ");


        // Avoiding owner updating offer not belong to him - by basil
        if (!Objects.equals(storeOffer.getStore().getOwner().getId(), ownerId)) throw new ApiException("Error: this store not belong to the owner");

        // avoiding updating offer when is it closed - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected")) throw new ApiException("Error: this store offer is closed. the store offer status ether is approved or rejected ");

        // avoiding update when offer Pending by individual response - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Pending by individual")) throw new ApiException("Error: this store offer is Pending by the individual you can't make approve.");

        storeOffer.setStatus("Approved");
        storeOffer.setUpdatedAt(LocalDateTime.now());
        storeOfferRepository.save(storeOffer);
        store.setStatus("Inactive");
        storeRepository.save(store);
        StoreContract storeContract = convertOfferToContract(offerId);
        rejectAllOffers(store.getStoreOffers());

        // Send to owner whatsapp message of approve
        sendApproveWhatsAppMessage(storeContract.getOwner().getPhoneNumber(),storeContract);
        // Send to individual whatsapp message of approve
        sendApproveWhatsAppMessage(storeContract.getIndividual().getPhoneNumber(),storeContract);
    }


    // owner reject the offer - by basil
    public void ownerReject( Integer offerId ,Integer ownerId){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(offerId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");

        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("Error: Owner Not Found");
        Store store = storeOffer.getStore();
        // Avoiding Owner make Reject when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(store.getId()) != null ) throw new ApiException("Error: store is sold you can't make Reject ");

        // Avoiding owner updating offer not belong to him - by basil
        if (!Objects.equals(storeOffer.getStore().getOwner().getId(), ownerId)) throw new ApiException("Error: this store not belong to the owner");

        // avoiding updating offer when is it closed - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected")) throw new ApiException("Error: this store offer is closed. the store offer status ether is approved or rejected ");

        storeOffer.setStatus("Rejected");
        storeOffer.setUpdatedAt(LocalDateTime.now());
        storeOfferRepository.save(storeOffer);

        // send to individual the offer is rejected
        sendRejectWhatsAppMessage(storeOffer.getIndividual().getPhoneNumber(),storeOffer);

    }
    // individual counter back the deal to owner - by basil
    public void individualCounterOffer( Integer offerId,Integer individualId , StoreCounterOfferDTOIn storeCounterOfferDTOIn){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(offerId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");
        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("Error: Individual Not Found");

        Store store = storeOffer.getStore();
        // Avoiding individual make counter back offer when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(store.getId()) != null ) throw new ApiException("Error: store is sold you can't make counter back offer ");

        // avoiding individual updating on store offer not belong to him - by basil
        if (!Objects.equals(storeOffer.getIndividual().getId(), individual.getId())) throw new ApiException("Error: this store offer not belong to the individual");

        // avoiding updating offer when is it closed - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected")) throw new ApiException("Error: this store offer is closed. the store offer status ether is approved or rejected ");

        // avoiding update when offer Pending by owner response - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Pending by owner")) throw new ApiException("Error: this store offer is Pending by the owner you can't make counter offer.");

        // avoiding individual make counter price greater than original Price - by basil
        Double originalPrice = storeOffer.getStore().getOriginalPrice();
        if (storeCounterOfferDTOIn.getCounterPrice() > originalPrice) throw new ApiException("Error: You can't make counter price greater than original Price.");


        storeOffer.setOfferPrice(storeCounterOfferDTOIn.getCounterPrice());
        storeOffer.setStatus("Pending by owner");
        storeOffer.setUpdatedAt(LocalDateTime.now());
        storeOfferRepository.save(storeOffer);

        // Create Message with note from the individual to owner for Negotiation - by basil
        Message message  = new Message();
        message.setStoreOffer(storeOffer);
        message.setIndividual(individual);
        message.setMessage(storeCounterOfferDTOIn.getMessage());
        messageRepository.save(message);
    }

    // individual approve the offer - by basil
    public void individualApprove(Integer offerId,Integer individualId){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(offerId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");
        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("Error: Individual Not Found");

        Store store = storeOffer.getStore();
        // Avoiding individual make approve when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(store.getId()) != null ) throw new ApiException("Error: store is sold you can't make approve ");

        // avoiding individual updating on store offer not belong to him - by basil
        if (!Objects.equals(storeOffer.getIndividual().getId(), individual.getId())) throw new ApiException("Error: this store offer not belong to the individual");

        // avoiding updating offer when is it closed - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected")) throw new ApiException("Error: this store offer is closed. the store offer status ether is approved or rejected ");


        // avoiding update when offer Pending by owner response - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Pending by owner")) throw new ApiException("Error: this store offer is Pending by the owner you can't approve the offer.");




        // set status Approved and convert offer to contract - by basil
        storeOffer.setStatus("Approved");
        storeOffer.setUpdatedAt(LocalDateTime.now());
        storeOfferRepository.save(storeOffer);
        StoreContract  storeContract = convertOfferToContract(offerId);
        store.setStatus("Inactive");
        storeRepository.save(store);
        rejectAllOffers(store.getStoreOffers());

        // Send to owner whatsapp message of approve
        sendApproveWhatsAppMessage(storeContract.getOwner().getPhoneNumber(),storeContract);
        // Send to individual whatsapp message of approve
        sendApproveWhatsAppMessage(storeContract.getIndividual().getPhoneNumber(),storeContract);
    }
    // individual Reject the offer - by basil
    public void individualReject(Integer offerId,Integer individualId){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(offerId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");
        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("Error: Individual Not Found");



        Store store = storeOffer.getStore();
        // Avoiding individual make Reject when store is sold - by basil
        if(storeContractRepository.findStoreContractByStoreId(store.getId()) != null ) throw new ApiException("Error: store is sold you can't make Reject ");


        // avoiding individual updating on store offer not belong to him - by basil
        if (!Objects.equals(storeOffer.getIndividual().getId(), individual.getId())) throw new ApiException("Error: this store offer not belong to the individual");

        // avoiding updating offer when is it closed - by basil
        if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected")) throw new ApiException("Error: this store offer is closed. the store offer status ether is approved or rejected ");

        // set status Rejected - by basil
        storeOffer.setStatus("Rejected");
        storeOffer.setUpdatedAt(LocalDateTime.now());
        storeOfferRepository.save(storeOffer);



    }

    public StoreContract convertOfferToContract(Integer OfferId){
        StoreOffer storeOffer = storeOfferRepository.findStoreOfferById(OfferId);
        if (storeOffer == null) throw new ApiException("Error: Store Offer Not Found");
        if (!storeOffer.getStatus().equalsIgnoreCase("Approved")) throw new ApiException("Error: this store offer is approved. you can't convert it to contract.");

        StoreContract storeContract = new StoreContract();


        storeContract.setStoreName(storeOffer.getStore().getStoreName());
        storeContract.setLocation(storeOffer.getStore().getLocation());
        storeContract.setWithEquipment(storeOffer.getStore().getWithEquipment());
        storeContract.setStoreType(storeOffer.getStore().getStoreType());
        storeContract.setNumberOfBranches(storeOffer.getStore().getNumberOfBranches());
        storeContract.setDescription(storeOffer.getStore().getDescription());
        storeContract.setAgreedPrice(storeOffer.getOfferPrice());
        storeContract.setContractDate(LocalDate.now());
        storeContract.setStore(storeOffer.getStore());
        storeContract.setIndividual(storeOffer.getIndividual());
        storeContract.setStoreOffer(storeOffer);
        storeContract.setOwner(storeOffer.getStore().getOwner());

        return storeContractRepository.save(storeContract);



    }

    private void sendApproveWhatsAppMessage(String to, StoreContract storeContract) {
        try {

            String message = String.format(
                    "تم الموافقة على بيع المحل تفاصيل العقد:\n" +
                            "اسم المتجر: %s\n" +
                            "الموقع: %s\n" +
                            "نوع المتجر: %s\n" +
                            "وصف: %s\n" +
                            "السعر المتفق عليه: %.2f ريال\n" +
                            "تاريخ العقد: %s\n" +
                            "اسم المالك: %s\n" +
                            "اسم الفرد: %s\n",
                    storeContract.getStoreName(),
                    storeContract.getLocation(),
                    storeContract.getStoreType(),
                    storeContract.getDescription(),
                    storeContract.getAgreedPrice(),
                    storeContract.getContractDate().toString(),
                    storeContract.getOwner().getFullName(),
                    storeContract.getIndividual().getFullName()
            );
            // Encode Arabic message to ensure compatibility
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

            Unirest.setTimeouts(5000, 10000); // 5 seconds to connect, 10 seconds for response
            HttpResponse<String> response = Unirest.post("https://api.ultramsg.com/instance102008/messages/chat")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("token", "1711dnot7uuo4nyu")
                    .field("to", to)
                    .field("body", message)

                    .asString();
            System.out.println(response.getBody());

        } catch (Exception e) {
            throw new ApiException("فشل في إرسال رسالة واتساب: " + e.getMessage());
        }
    }
    private void sendRejectWhatsAppMessage(String to, StoreOffer storeOffer) {
        try {

            String message = String.format(
                    "نأسف لإبلاغك بأن عرضك قد تم رفضه. إليك تفاصيل العرض والمتجر:\n\n" +
                            "تفاصيل المتجر:\n" +
                            "اسم المتجر: %s\n" +
                            "اسم مالك المتجر: %s\n" +
                            "الموقع: %s\n" +
                            "نوع المتجر: %s\n" +
                            "عدد الفروع: %d\n" +
                            "السعر الأصلي: %.2f ريال\n" +
                            "وصف المتجر: %s\n" +
                            "حالة المتجر: %s\n\n" +
                            "تفاصيل العرض:\n" +
                            "السعر المعروض: %.2f ريال\n" +
                            "نفس الصفقة: %s\n" +
                            "حالة العرض: %s\n",
                    storeOffer.getStore().getStoreName(),
                    storeOffer.getStore().getOwner().getFullName(),
                    storeOffer.getStore().getLocation(),
                    storeOffer.getStore().getStoreType(),
                    storeOffer.getStore().getNumberOfBranches(),
                    storeOffer.getStore().getOriginalPrice(),
                    storeOffer.getStore().getDescription(),
                    storeOffer.getStore().getStatus(),
                    storeOffer.getOfferPrice(),
                    storeOffer.getSameDeal() ? "نعم" : "لا",
                    storeOffer.getStatus()
            );
            // Encode Arabic message to ensure compatibility
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

            Unirest.setTimeouts(5000, 10000); // 5 seconds to connect, 10 seconds for response
            HttpResponse<String> response = Unirest.post("https://api.ultramsg.com/instance102008/messages/chat")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("token", "1711dnot7uuo4nyu")
                    .field("to", to)
                    .field("body", message)

                    .asString();
            System.out.println(response.getBody());

        } catch (Exception e) {
            throw new ApiException("فشل في إرسال رسالة واتساب: " + e.getMessage());
        }
    }
    public void rejectAllOffers(Set<StoreOffer> storeOffers){

        for (StoreOffer storeOffer : storeOffers) {
            if (storeOffer.getStatus().equalsIgnoreCase("Approved") || storeOffer.getStatus().equalsIgnoreCase("Rejected") ) continue;
            storeOffer.setStatus("Rejected");
            storeOfferRepository.save(storeOffer);
            // send to individual the offer is rejected
            sendRejectWhatsAppMessage(storeOffer.getIndividual().getPhoneNumber(),storeOffer);
        }
    }

    public StoreCounterOfferDTO convertToStoreCounterOfferDTO(StoreOffer storeOffer){
        Store store = storeOffer.getStore();
        Individual individual = storeOffer.getIndividual();

        return new StoreCounterOfferDTO(store.getStoreName(),store.getLocation(),store.getWithEquipment(),store.getStoreType(),store.getNumberOfBranches(),store.getOriginalPrice(),store.getDescription(),storeOffer.getOfferPrice(),storeOffer.getStatus() , individual.getFullName());
    }
}
