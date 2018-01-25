package daggerok.domain

import spock.lang.Specification

class CreditCardRepositoryTest extends Specification {

  final CreditCardRepository creditCardRepository = new CreditCardRepository()

  def 'should be able to save and fetch credit card'() {
    given:
      UUID id = UUID.randomUUID()
    and:
       CreditCard creditCard = new CreditCard(id)
    and:
       BigDecimal limit = 100
    and:
      creditCard.assignLimit(limit)
    when:
      creditCardRepository.save(creditCard)
    then:
      CreditCard savedCreditCard = creditCardRepository.findById(id)
    and:
      savedCreditCard.availableLimit() == limit
  }
}
