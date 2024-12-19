
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


---

**Notes:**

- **Service Associations:**
  - All methods related to franchise offers are handled by `FranchiseOfferService`.
  - Contract-related methods for individuals are handled by `IndividualService`.
  - Contract-related methods for owners are handled by `OwnerService`.

- **Repository Associations:**
  - Each service interacts with its respective repository for data persistence.

- **Method Naming Conventions:**
  - Controller methods are clearly named to indicate their purpose and the actor involved (`individual` or `owner`).
  - Service methods correspond directly to their controller counterparts for consistency and maintainability.

- **HTTP Status Codes:**
  - All responses retain the status code `200` as per your instruction.

---

If you need further assistance or additional mappings, feel free to let me know!
