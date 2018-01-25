package daggerok.domain

import spock.lang.Specification

class CreditCardTest extends Specification {

  final CreditCard creditCard = new CreditCard(UUID.randomUUID())

  def 'cant assign limit for second time'() {
    given:
      creditCard.assignLimit(100)
    when:
      creditCard.assignLimit(200)
    then:
      thrown(IllegalStateException)
  }

  def 'card should has assigned limit'() {
    when:
      creditCard.assignLimit(100 as BigDecimal)
    then:
      creditCard.availableLimit() == 100
  }

  def 'cannot withdraw over limit'() {
    given:
      creditCard.assignLimit(100 as BigDecimal)
    when:
      creditCard.withdraw(150 as BigDecimal)
    then:
      thrown(IllegalStateException)
  }

  def 'can withdraw money'() {
    given:
      creditCard.assignLimit(100 as BigDecimal)
    when:
      creditCard.withdraw(50 as BigDecimal)
    then:
      creditCard.availableLimit() == 50
  }

  def 'can repay a money'() {
    given:
      creditCard.assignLimit(100 as BigDecimal)
    and:
      creditCard.withdraw(50 as BigDecimal)
    when:
      creditCard.repay(50 as BigDecimal)
    then:
      creditCard.availableLimit() == 100
  }
}
