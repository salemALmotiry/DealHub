package com.example.capstone3.Service;

import com.example.capstone3.ApiResponse.ApiException;
import com.example.capstone3.InDTO.FranchiseOfferIn;
import com.example.capstone3.Model.*;
import com.example.capstone3.OutDTO.FranchiseOfferDTO;
import com.example.capstone3.Repository.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


// Done by salem

@Service
@AllArgsConstructor
public class  FranchiseOfferService {

    private final FranchiseOfferRepository franchiseOfferRepository;
    private final IndividualRepository individualRepository;
    private final FranchiseRepository franchiseRepository;
    private final MessageRepository messageRepository;
    private final OwnerRepository ownerRepository;
    private final FranchiseContractRepository franchiseContractRepository;


    // Individual Offer flow
    public void individualMakeOfferSameDeal(Integer individualId,Integer franchiseId) {
        // Retrieve the individual by ID
        Individual individual = individualRepository.findIndividualById(individualId);

        if (individual == null) throw new ApiException("Individual not found");


        Franchise franchise = franchiseRepository.findFranchiseById(franchiseId);

        if (franchise == null) throw new ApiException("Franchise not found");


        List<FranchiseOffer> franchiseOffers = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Pending by owner");
        List<FranchiseOffer> franchiseOfferPendingByIndividual = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Pending by individual");
        List<FranchiseOffer> franchiseOfferApproved = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Approved");

        if (!franchiseOfferApproved.isEmpty() || !franchiseOffers.isEmpty() || !franchiseOfferPendingByIndividual.isEmpty()) throw new ApiException("you have active offer");


        FranchiseOffer franchiseOffer = new FranchiseOffer();

        franchiseOffer.setFranchise(franchise);

        franchiseOffer.setSameDeal(true);
        franchiseOffer.setOfferedFee(franchise.getFranchiseFee());
        franchiseOffer.setContractDuration(franchise.getContractDuration());
        franchiseOffer.setInvestmentAmount(franchise.getInvestmentAmount());
        franchiseOffer.setOngoingAdminFees(franchise.getOngoingAdminFees());
        franchiseOffer.setIndividual(individual);

        franchiseOffer.setStatus("Pending by owner");

        franchiseOfferRepository.save(franchiseOffer);




    }


    public void individualMakeOfferWithCounter (Integer individualId, Integer franchiseId, FranchiseOfferIn franchiseOfferIn){

        // Retrieve the individual by ID
        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("Individual not found");


        Franchise franchise = franchiseRepository.findFranchiseById(franchiseId);

        if (franchise == null) throw new ApiException("Franchise not found");


        List<FranchiseOffer> franchiseOffers = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Pending by owner");
        List<FranchiseOffer> franchiseOfferPendingByIndividual = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Pending by individual");
        List<FranchiseOffer> franchiseOfferApproved = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Approved");

        if (!franchiseOfferApproved.isEmpty() || !franchiseOffers.isEmpty() || !franchiseOfferPendingByIndividual.isEmpty()) throw new ApiException("you have active offer");

        FranchiseOffer franchiseOffer = new FranchiseOffer();

        // Set fields in franchiseOffer based on FranchiseOfferIn
        franchiseOffer.setFranchise(franchise);
        franchiseOffer.setSameDeal(false);
        franchiseOffer.setOfferedFee(franchise.getFranchiseFee());
        franchiseOffer.setCounterOfferFee(franchiseOfferIn.getCounterOfferFee());
        franchiseOffer.setContractDuration(franchise.getContractDuration());
        franchiseOffer.setCounterContractDuration(franchiseOfferIn.getCounterContractDuration());
        franchiseOffer.setInvestmentAmount(franchise.getInvestmentAmount());
        franchiseOffer.setCounterInvestmentAmount(franchiseOfferIn.getCounterInvestmentAmount());
        franchiseOffer.setOngoingAdminFees(franchise.getOngoingAdminFees());
        franchiseOffer.setCounterOngoingAdminFees(franchiseOfferIn.getCounterOngoingAdminFees());
        franchiseOffer.setIndividual(individual);
        franchiseOffer.setStatus("Pending by owner");
        franchiseOfferRepository.save(franchiseOffer);

        // Create Message with note from the individual to owner for Negotiation
        com.example.capstone3.Model.Message mesg = new com.example.capstone3.Model.Message();

        mesg.setFranchiseOffer(franchiseOffer);
        mesg.setIndividual(individual);
        mesg.setMessage(franchiseOfferIn.getMessage());
        messageRepository.save(mesg);

    }


    public List<FranchiseOfferDTO> getIndividualFranchiseOfferPendingByOwner(Integer individualId){
        Individual individual = individualRepository.findIndividualById(individualId);

        if (individual == null) throw new ApiException("Individual not found");


        // Retrieve franchise offers with the specified status
        List<FranchiseOffer> franchiseOffers = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Pending by owner");

        if (franchiseOffers.isEmpty()) {
            throw new ApiException("No pending franchise offers found for the owner");
        }

        // Map all FranchiseOffer objects to DTOs
        return franchiseOffers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }


    public List<FranchiseOfferDTO> getIndividualFranchiseOfferWaitResponse(Integer individualId) {
        Individual individual = individualRepository.findIndividualById(individualId);

        if (individual == null) throw new ApiException("Individual not found");


        // Retrieve franchise offers with the specified status
        List<FranchiseOffer> franchiseOffers = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(individualId, "Pending by individual");

        if (franchiseOffers.isEmpty()) {
            throw new ApiException("No pending franchise offers found for the individual");
        }

        // Map all FranchiseOffer objects to DTOs
        return franchiseOffers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    public void individualCounterOwnerOffer(Integer individualId,Integer offerId, Integer franchiseId, FranchiseOfferIn franchiseOfferIn){
        // Retrieve the individual by ID
        Individual individual = individualRepository.findIndividualById(individualId);

        if (individual == null) throw new ApiException("individual not found");


        Franchise franchise = franchiseRepository.findFranchiseById(franchiseId);

        if (franchise == null) throw new ApiException("Franchise not found");


        FranchiseOffer oldFranchiseOffer = franchiseOfferRepository.findFranchiseOfferById(offerId);

        if (oldFranchiseOffer == null) throw new ApiException("Franchise Offer not found");

        if (oldFranchiseOffer.getStatus().equals("Approved") ) throw new ApiException("Cannot counter offer that is approved");

        if (oldFranchiseOffer.getStatus().equals("Rejected") ) throw new ApiException("Cannot counter offer that is Rejected");

        if (oldFranchiseOffer.getStatus().equals("Pending by owner") ) throw new ApiException("Offer pending by owner");

        if (oldFranchiseOffer.isSameDeal()) throw new ApiException("Cannot counter same deal");







        // Set fields in franchiseOffer based on FranchiseOfferIn
        oldFranchiseOffer.setSameDeal(oldFranchiseOffer.isSameDeal());
        oldFranchiseOffer.setOfferedFee(oldFranchiseOffer.getCounterOfferFee());
        oldFranchiseOffer.setContractDuration(oldFranchiseOffer.getCounterContractDuration());
        oldFranchiseOffer.setInvestmentAmount(oldFranchiseOffer.getCounterInvestmentAmount());
        oldFranchiseOffer.setOngoingAdminFees(oldFranchiseOffer.getCounterOngoingAdminFees());

        oldFranchiseOffer.setCounterOfferFee(franchiseOfferIn.getCounterOfferFee());
        oldFranchiseOffer.setCounterContractDuration(franchiseOfferIn.getCounterContractDuration());
        oldFranchiseOffer.setCounterInvestmentAmount(franchiseOfferIn.getCounterInvestmentAmount());
        oldFranchiseOffer.setCounterOngoingAdminFees(franchiseOfferIn.getCounterOngoingAdminFees());
        oldFranchiseOffer.setStatus("Pending by owner");
        oldFranchiseOffer.setUpdatedAt(LocalDateTime.now());


        franchiseOfferRepository.save(oldFranchiseOffer);

    }


    public void individualApprovedOffer(Integer franchiseOfferId,Integer individualId){
        FranchiseOffer franchiseOffer = franchiseOfferRepository.findFranchiseOfferByFranchiseIdAndIndividualId(franchiseOfferId,individualId);
        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) throw new ApiException("individual not found");


        if (franchiseOffer == null) throw new ApiException("Franchise Offer not found");


        if (franchiseOffer.getStatus().equals("Pending by owner")) {
            throw new ApiException("Cannot accept offer that is pending by owner");
        }
        FranchiseContracts franchiseContracts = new FranchiseContracts();

        if (franchiseOffer.getStatus().equals("Pending by individual")  && franchiseOffer.isSameDeal()) {


            franchiseContracts.setFranchiseeName(franchiseOffer.getFranchise().getBrandName());
            franchiseContracts.setFranchisorName(franchiseOffer.getFranchise().getOwner().getFullName());
            franchiseContracts.setFranchisorPhone(franchiseOffer.getFranchise().getOwner().getPhoneNumber());


            franchiseContracts.setSecondPartyName(franchiseOffer.getIndividual().getFullName());
            franchiseContracts.setSecondPartyPhone(franchiseOffer.getIndividual().getPhoneNumber());

            franchiseContracts.setContractDuration(franchiseOffer.getContractDuration());

            franchiseContracts.setInvestmentAmount(franchiseOffer.getInvestmentAmount());
            franchiseContracts.setOngoingAdminFees(franchiseOffer.getOngoingAdminFees());
            franchiseContracts.setAgreedFee(franchiseOffer.getOfferedFee());
            franchiseContracts.setContractDate(LocalDate.now());
            franchiseContracts.setStatus("Active");


            franchiseContracts.setFranchise(franchiseOffer.getFranchise());
            franchiseContracts.setFranchiseOffer(franchiseOffer);
            franchiseContracts.setOwner(franchiseOffer.getFranchise().getOwner());
            franchiseContracts.setIndividual(franchiseOffer.getIndividual());

            franchiseContractRepository.save(franchiseContracts);


            franchiseOffer.setStatus("Approved");
            franchiseOfferRepository.save(franchiseOffer);

            sendWhatsAppMessage(franchiseContracts.getFranchise().getOwner().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to owner.");

            sendWhatsAppMessage(franchiseContracts.getIndividual().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to individual.");

        }
        else if(franchiseOffer.getStatus().equals("Pending by individual") ){

            franchiseContracts.setFranchiseeName(franchiseOffer.getFranchise().getBrandName());
            franchiseContracts.setFranchisorName(franchiseOffer.getFranchise().getOwner().getFullName());
            franchiseContracts.setFranchisorPhone(franchiseOffer.getFranchise().getOwner().getPhoneNumber());

            franchiseContracts.setSecondPartyName(franchiseOffer.getIndividual().getFullName());
            franchiseContracts.setSecondPartyPhone(franchiseOffer.getIndividual().getPhoneNumber());

            franchiseContracts.setContractDuration(franchiseOffer.getCounterContractDuration());

            franchiseContracts.setInvestmentAmount(franchiseOffer.getCounterInvestmentAmount());
            franchiseContracts.setOngoingAdminFees(franchiseOffer.getCounterOngoingAdminFees());
            franchiseContracts.setAgreedFee(franchiseOffer.getCounterOfferFee());
            franchiseContracts.setContractDate(LocalDate.now());
            franchiseContracts.setStatus("Active");


            franchiseContracts.setFranchise(franchiseOffer.getFranchise());
            franchiseContracts.setFranchiseOffer(franchiseOffer);
            franchiseContracts.setOwner(franchiseOffer.getFranchise().getOwner());
            franchiseContracts.setIndividual(franchiseOffer.getIndividual());

            franchiseContractRepository.save(franchiseContracts);

            franchiseOffer.setStatus("Approved");
            franchiseOfferRepository.save(franchiseOffer);

            sendWhatsAppMessage(franchiseContracts.getFranchise().getOwner().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to owner.");

            sendWhatsAppMessage(franchiseContracts.getIndividual().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to individual.");

        }


    }

    public void individualRejectedOffer(Integer franchiseOfferId,Integer individualId){

        FranchiseOffer franchiseOffer = franchiseOfferRepository.findFranchiseOfferById(franchiseOfferId);
        Individual individual = individualRepository.findIndividualById(individualId);
        if (individual == null) {
            throw new ApiException("Individual not found");
        }

        if (franchiseOffer == null) {
            throw new ApiException("Franchise Offer not found");
        }

        if (franchiseOffer.getStatus().equals("Approved")) {
            throw new ApiException("Cannot reject offer that is Approved");
        }
        if (franchiseOffer.getStatus().equals("Rejected")) {
            throw new ApiException("Cannot reject offer that is Rejected");
        }
        franchiseOffer.setStatus("Rejected");
        franchiseOfferRepository.save(franchiseOffer);

        sendWhatsAppMessageReject(franchiseOffer.getFranchise().getOwner().getPhoneNumber(), franchiseOffer);
        System.out.println("Message sent to owner.");

        sendWhatsAppMessageReject(franchiseOffer.getIndividual().getPhoneNumber(), franchiseOffer);
        System.out.println("Message sent to individual.");
    }



    // Owner offer flow

    public List<FranchiseOfferDTO> getOwnerFranchiseOfferWaitResponse(Integer ownerId,Integer franchiseId){
        Owner owner = ownerRepository.findOwnerById(ownerId);
        Franchise franchise = franchiseRepository.findFranchiseByIdAndOwnerId(franchiseId,ownerId);
        if (owner == null) {
            throw new ApiException("owner not found");
        }
        if (franchise == null) {
            throw new ApiException("Franchise not found");
        }

        List<FranchiseOffer> franchiseOffer = franchiseOfferRepository.findFranchiseOfferByFranchiseIdAndStatus(franchiseId,"Pending by owner");

        // Map all FranchiseOffer objects to DTOs
        return franchiseOffer.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }


    public List<FranchiseOfferDTO> getOwnerFranchiseOfferPendingByIndividual(Integer ownerId){
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) {
            throw new ApiException("owner not found");
        }

        List<FranchiseOffer> franchiseOffer = franchiseOfferRepository.findFranchiseOfferByIndividualIdAndStatus(ownerId,"Pending by individual");

        // Map all FranchiseOffer objects to DTOs
        return franchiseOffer.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }


    public void ownerAcceptFranchiseOffer(Integer franchiseOfferId, Integer ownerId) {
        FranchiseOffer franchiseOffer = franchiseOfferRepository.findFranchiseOfferById(franchiseOfferId);
        Owner owner = ownerRepository.findOwnerById(ownerId);

        if (owner == null) {
            throw new ApiException("Owner not found");
        }

        if (franchiseOffer == null) {
            throw new ApiException("Franchise Offer not found");
        }

        if (franchiseOffer.getStatus().equals("Rejected")) {
            throw new ApiException("Cannot accept offer that is Rejected");
        }

        if (franchiseOffer.getStatus().equals("Approved")) {
            throw new ApiException("Cannot accept offer that is Approved");
        }

        if (franchiseOffer.getStatus().equals("Pending by individual")) {
            throw new ApiException("Cannot accept offer that is pending by individual");
        }

        FranchiseContracts franchiseContracts = new FranchiseContracts();

        if (franchiseOffer.getStatus().equals("Pending by owner") && franchiseOffer.isSameDeal()) {
            franchiseContracts.setFranchiseeName(franchiseOffer.getFranchise().getBrandName());
            franchiseContracts.setFranchisorName(owner.getFullName());
            franchiseContracts.setFranchisorPhone(owner.getPhoneNumber());
            franchiseContracts.setSecondPartyName(franchiseOffer.getIndividual().getFullName());
            franchiseContracts.setSecondPartyPhone(franchiseOffer.getIndividual().getPhoneNumber());
            franchiseContracts.setContractDuration(franchiseOffer.getContractDuration());
            franchiseContracts.setInvestmentAmount(franchiseOffer.getInvestmentAmount());
            franchiseContracts.setOngoingAdminFees(franchiseOffer.getOngoingAdminFees());
            franchiseContracts.setAgreedFee(franchiseOffer.getOfferedFee());
            franchiseContracts.setContractDate(LocalDate.now());
            franchiseContracts.setStatus("Active");
            franchiseContracts.setFranchise(franchiseOffer.getFranchise());
            franchiseContracts.setFranchiseOffer(franchiseOffer);
            franchiseContracts.setOwner(owner);
            franchiseContracts.setIndividual(franchiseOffer.getIndividual());

            franchiseContractRepository.save(franchiseContracts);
            franchiseOffer.setStatus("Approved");
            franchiseOfferRepository.save(franchiseOffer);

            sendWhatsAppMessage(franchiseContracts.getFranchise().getOwner().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to owner.");

            sendWhatsAppMessage(franchiseContracts.getIndividual().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to individual.");


        } else if (franchiseOffer.getStatus().equals("Pending by owner")) {
            franchiseContracts.setFranchiseeName(franchiseOffer.getFranchise().getBrandName());
            franchiseContracts.setFranchisorName(owner.getFullName());
            franchiseContracts.setFranchisorPhone(owner.getPhoneNumber());
            franchiseContracts.setSecondPartyName(franchiseOffer.getIndividual().getFullName());
            franchiseContracts.setSecondPartyPhone(franchiseOffer.getIndividual().getPhoneNumber());
            franchiseContracts.setContractDuration(franchiseOffer.getCounterContractDuration());
            franchiseContracts.setInvestmentAmount(franchiseOffer.getCounterInvestmentAmount());
            franchiseContracts.setOngoingAdminFees(franchiseOffer.getCounterOngoingAdminFees());
            franchiseContracts.setAgreedFee(franchiseOffer.getCounterOfferFee());
            franchiseContracts.setContractDate(LocalDate.now());
            franchiseContracts.setStatus("Active");
            franchiseContracts.setFranchise(franchiseOffer.getFranchise());
            franchiseContracts.setFranchiseOffer(franchiseOffer);
            franchiseContracts.setOwner(owner);
            franchiseContracts.setIndividual(franchiseOffer.getIndividual());

            franchiseContractRepository.save(franchiseContracts);
            franchiseOffer.setStatus("Approved");
            franchiseOfferRepository.save(franchiseOffer);


            sendWhatsAppMessage(franchiseContracts.getFranchise().getOwner().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to owner.");

            sendWhatsAppMessage(franchiseContracts.getIndividual().getPhoneNumber(), franchiseContracts);
            System.out.println("Message sent to individual.");

        }
    }

    public void ownerMakeFranchiseCounterForIndividual (Integer ownerId,Integer offerId, Integer franchiseId, FranchiseOfferIn franchiseOfferIn){

        // Retrieve the individual by ID
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) throw new ApiException("owner not found");


        Franchise franchise = franchiseRepository.findFranchiseByIdAndOwnerId(franchiseId,ownerId);

        if (franchise == null) throw new ApiException("Franchise not found");


        FranchiseOffer oldFranchiseOffer = franchiseOfferRepository.findFranchiseOfferById(offerId);

        if (oldFranchiseOffer == null) throw new ApiException("Franchise Offer not found");

        if (oldFranchiseOffer.isSameDeal()) throw new ApiException("Cannot counter same deal");

        if (oldFranchiseOffer.getStatus().equals("Pending by individual") ) throw new ApiException("Cannot offer same deal again");

        if (oldFranchiseOffer.getStatus().equals("Approved") ) throw new ApiException("Cannot counter offer that is approved");

        if (oldFranchiseOffer.getStatus().equals("Rejected") ) throw new ApiException("Cannot counter offer that is Rejected");



        // Set fields in franchiseOffer based on FranchiseOfferIn
        oldFranchiseOffer.setSameDeal(oldFranchiseOffer.isSameDeal());
        oldFranchiseOffer.setOfferedFee(oldFranchiseOffer.getCounterOfferFee());
        oldFranchiseOffer.setContractDuration(oldFranchiseOffer.getCounterContractDuration());
        oldFranchiseOffer.setInvestmentAmount(oldFranchiseOffer.getCounterInvestmentAmount());
        oldFranchiseOffer.setOngoingAdminFees(oldFranchiseOffer.getCounterOngoingAdminFees());

        oldFranchiseOffer.setCounterOfferFee(franchiseOfferIn.getCounterOfferFee());
        oldFranchiseOffer.setCounterContractDuration(franchiseOfferIn.getCounterContractDuration());
        oldFranchiseOffer.setCounterInvestmentAmount(franchiseOfferIn.getCounterInvestmentAmount());
        oldFranchiseOffer.setCounterOngoingAdminFees(franchiseOfferIn.getCounterOngoingAdminFees());
        oldFranchiseOffer.setStatus("Pending by individual");
        oldFranchiseOffer.setUpdatedAt(LocalDateTime.now());


        franchiseOfferRepository.save(oldFranchiseOffer);
    }

    public void ownerRejectFranchiseOffer(Integer franchiseOfferId,Integer ownerId){
        FranchiseOffer franchiseOffer = franchiseOfferRepository.findFranchiseOfferById(franchiseOfferId);
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if (owner == null) {
            throw new ApiException("owner not found");
        }
        if (franchiseOffer == null) {
            throw new ApiException("Franchise Offer not found");
        }
        if (franchiseOffer.getStatus().equals("Rejected") ) throw new ApiException("Cannot rejected offer that is Rejected");
        if (franchiseOffer.getStatus().equals("Approved") ) throw new ApiException("Cannot rejected offer that is Approved");

        franchiseOffer.setStatus("Rejected");
        franchiseOfferRepository.save(franchiseOffer);

        sendWhatsAppMessageReject(franchiseOffer.getFranchise().getOwner().getPhoneNumber(), franchiseOffer);
        System.out.println("Message sent to owner.");

        sendWhatsAppMessageReject(franchiseOffer.getIndividual().getPhoneNumber(), franchiseOffer);
        System.out.println("Message sent to individual.");
    }





    private void sendWhatsAppMessage(String to, FranchiseContracts franchiseContracts) {
        try {
            // Prepare the message
            String message = String.format(
                    "تمت الموافقة على عقد الفرنشايز بنجاح! \n" +
                            "اسم العلامة التجارية: %s\n" +
                            "اسم المالك: %s\n" +
                            "رقم المالك: %s\n" +
                            "اسم الطرف الثاني: %s\n" +
                            "رقم الطرف الثاني: %s\n" +
                            "مدة العقد: %s سنوات\n" +
                            "قيمة الاستثمار: %.2f ريال\n" +
                            "رسوم الإدارة: %.2f ريال\n" +
                            "الرسوم المتفق عليها: %.2f ريال\n" +
                            "تاريخ العقد: %s\n" +
                            "حالة العقد: %s",
                    franchiseContracts.getFranchiseeName(),
                    franchiseContracts.getFranchisorName(),
                    franchiseContracts.getFranchisorPhone(),
                    franchiseContracts.getSecondPartyName(),
                    franchiseContracts.getSecondPartyPhone(),
                    franchiseContracts.getContractDuration(),
                    franchiseContracts.getInvestmentAmount(),
                    franchiseContracts.getOngoingAdminFees(),
                    franchiseContracts.getAgreedFee(),
                    franchiseContracts.getContractDate().toString(),
                    franchiseContracts.getStatus()
            );

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


    private void sendWhatsAppMessageReject(String to, FranchiseOffer franchiseOffer) {
        try {
            // تحضير الرسالة لرفض العرض
            String message = String.format(
                    "تم رفض عرض عقد الفرنشايز.\n" +
                            "اسم العلامة التجارية: %s\n" +
                            "اسم المالك: %s\n" +
                            "مدة العرض: %s سنوات\n" +
                            "قيمة الاستثمار: %.2f ريال\n" +
                            "الرسوم المتفق عليها: %.2f ريال\n" +
                            "حالة العرض: %s",
                    franchiseOffer.getFranchise().getBrandName(),
                    franchiseOffer.getFranchise().getOwner().getFullName(),
                    franchiseOffer.getContractDuration(),
                    franchiseOffer.getInvestmentAmount(),
                    franchiseOffer.getOfferedFee(),
                    "مرفوض"
            );


            Unirest.setTimeouts(5000, 10000); // 5 ثوانٍ للاتصال، و10 ثوانٍ للاستجابة
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


    public FranchiseOfferDTO mapToDto(FranchiseOffer franchiseOffer) {
        if (franchiseOffer == null) {
            return null;
        }

        FranchiseOfferDTO dto = new FranchiseOfferDTO();

        // Mapping basic fields
        dto.setFranchiseName(franchiseOffer.getFranchise().getBrandName());
        dto.setOfferedBy(franchiseOffer.getIndividual().getFullName());
        dto.setOfferedFee(franchiseOffer.getOfferedFee());
        dto.setCounterOfferFee(franchiseOffer.getCounterOfferFee());
        dto.setContractDuration(franchiseOffer.getContractDuration());
        dto.setCounterContractDuration(franchiseOffer.getCounterContractDuration());
        dto.setInvestmentAmount(franchiseOffer.getInvestmentAmount());
        dto.setCounterInvestmentAmount(franchiseOffer.getCounterInvestmentAmount());
        dto.setOngoingAdminFees(franchiseOffer.getOngoingAdminFees());
        dto.setCounterOngoingAdminFees(franchiseOffer.getCounterOngoingAdminFees());
        dto.setSameDeal(franchiseOffer.isSameDeal());
        dto.setStatus(franchiseOffer.getStatus());

        return dto;
    }

}
