| **Controller Method Name**                        | **Service Method Name**                               |
|----------------------------------------------------|-------------------------------------------------------|
| makeFranchiseOffer                                 | individualMakeOfferSameDeal                           |
| makeOfferWithCounter                               | individualMakeOfferWithCounter                        |
| getOfferPendingByOwner                             | getIndividualFranchiseOfferPendingByOwner             |
| getOfferWaitResponse (Individual)                  | getIndividualFranchiseOfferWaitResponse               |
| individualCounterOwnerOffer                        | individualCounterOwnerOffer                           |
| individualApprovedOffer                            | individualApprovedOffer                               |
| individualRejectedOffer                            | individualRejectedOffer                               |
| getOfferWaitResponse (Owner)                       | getOwnerFranchiseOfferWaitResponse                    |
| acceptOffer                                        | ownerAcceptFranchiseOffer                             |
| makeOfferWithCounter (Owner)                       | ownerMakeFranchiseCounterForIndividual                |
| ownerRejectFranchiseOffer                          | ownerRejectFranchiseOffer                             |
| sendMessageForOwnerAcceptOffer                     | sendMessageForOwnerAcceptOffer                        |
| sendMessageRejectForOwnerRejectOffer               | sendMessageRejectForOwnerRejectOffer                  |
| sendMessageForIndividualAcceptOffer                | sendMessageForIndividualAcceptOffer                   |
| sendMessageRejectForIndividualRejectOffer          | sendMessageRejectForIndividualRejectOffer             |
| getActiveContractsForIndividual                    | getActiveContractsForIndividual                       |
| getExpiredContractsForIndividual                   | getExpiredContractsForIndividual                      |
| getActiveContracts (Owner)                         | getActiveContractsOwner                               |
| getExpiredContracts (Owner)                        | getExpiredContractsOwner                              |

### Controller Names:

- FranchiseOfferController
- IndividualController
- OwnerController
- FranchiseController
- FranchiseContractController

### Service Names:

- FranchiseOfferService
- IndividualService
- OwnerService
- FranchiseService
- FranchiseContractService

### Repository Names:

- FranchiseOfferRepository
- IndividualRepository
- OwnerRepository
- FranchiseRepository
- FranchiseContractRepository
