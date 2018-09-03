@Wishlist
Feature: Bunnings Wishlist feature
  
  
  Scenario: Add paint product to wishlist
    Given I am on bunnings search landing page
    When I navigate through search page with paint product
    And I click on the first paint result in the products page
    Then I should land on product details page and add product to wishlist
    Then I should see the wishlist with a product added
    
