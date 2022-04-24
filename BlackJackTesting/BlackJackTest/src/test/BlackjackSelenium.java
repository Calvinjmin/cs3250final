package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

 

class BlackjackSelenium 
{
   private WebDriver driver;
   // This is our Blackjack website we created and hosted.
   private String url = "http://cjm9vr.pythonanywhere.com/";
   
   @BeforeEach
   void setUp() throws Exception 
   {
      // configure path to the driver
      System.setProperty("webdriver.chrome.driver", "/Users/kiyanzewer/Documents/cs3250final/chromedriver/");
          
      // create an instance of the web browser and open it    
      driver = new ChromeDriver();   
      
      // open the given url 
      driver.get(url);               
   }

   @AfterEach
   void tearDown() throws Exception 
   {
      // close the browser
      driver.close();           
   }

   
   /*
    * Kiyan Zewer test 1/20
    * The Title should be '- Blackjack'
    */
   @Test
   public void testTitle() {
	   assertEquals(driver.getTitle(), "- Blackjack");
   }
   
   /*
    * Kiyan Zewer test 2/20
    * Clicking Place Bet should result in a player total less than 22 
    * as 21 is the largest possible starting hand
    */
   @Test
   public void testResetPlayerTotal() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   int bound = 22;
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   assertTrue(bound > playerTotal);
   }
   
   /*
    * Kiyan Zewer test 3/20
    * Clicking hit as the first move should always result in a new player total value
    */
   @Test
   public void testHit() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   while(e.getText().equals("Player Total - 21") || f.getText().equals("Dealer Total - 21")) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   }
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   driver.findElement(By.name("hitbutton")).click();
	   WebElement g = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int newPlayerTotal = Integer.parseInt(g.getText().substring(g.getText().indexOf("-") + 2));
	   assertTrue(playerTotal != newPlayerTotal);
   }
   
   /*
    * Kiyan Zewer test 4/20
    * Clicking stand should change the Dealer Hand from hidden to shown
    */
   @Test
   public void testStandShowsText() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   String dealer = e.getText();
	   String player = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   while(dealer.equals("Dealer Total - 21") || player.equals("Player Total - 21")){
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   dealer = e.getText();
		   player = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   }
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   assertFalse(f.getText().contains("HIDDEN"));
   }
   
   /*
    * Kiyan Zewer test 5/20
    * Clicking the stand button should keep the same Player Total value
    */
   @Test
   public void testStandKeepsSameValue() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int newPlayerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   assertEquals(playerTotal, newPlayerTotal);
   }
   
   /*
    * Kiyan Zewer test 6/20
    * If the dealer has blackjack the game should show their cards and show 
    * the total as 21 not hidden and that they won as long as the dealer did not also get a Blackjack
    */
   @Test
   public void dealerBlackJack(){
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   String dealerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText();
	   while(!driver.getPageSource().contains("Dealer Blackjack")){
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   dealerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText();
	   }
	   assertTrue(driver.getPageSource().contains("Dealer Blackjack") && dealerTotal.contains("21"));
   }
   
   /*
    * Kiyan Zewer test 7/20
    * If the player has blackjack the game should show their total as 21
    */
   @Test
   public void playerBlackJack(){
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   String playerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   while(!playerTotal.contains("21")){
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   playerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   }
	   assertTrue(driver.getPageSource().contains("Player Blackjack") && playerTotal.contains("Player Total - 21"));
   }
   
   /*
    * Kiyan Zewer test 8/20
    * If the player clicks stand then the hit button should have no function
    * therefore, their total will be the same after clicking hit
    */
   @Test
   public void standThenHit() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   while(driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText().equals("Dealer Total - 21")) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   }
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   driver.findElement(By.xpath("//button[text()='Hit']")).click();
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int newPlayerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   assertEquals(playerTotal, newPlayerTotal);
   }
   
   /*
    * Kiyan Zewer test 9/20
    * Clicking hit after you bust will not do anything as the game is over
    */
   @Test
   public void hitUntilBust() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   while(f.getText().equals("Dealer Total - 21") || playerTotal == 21) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   }
	   while(playerTotal < 21){
		   driver.findElement(By.xpath("//button[text()='Hit']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   }
	   driver.findElement(By.xpath("//button[text()='Hit']")).click();
	   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int newPlayerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   assertEquals(playerTotal, newPlayerTotal);
   }
   
   /*
    * Kiyan Zewer test 10/20
    * if the card is diamond the text is red
    */
   @Test
   public void diamondIsRed() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   while(!driver.getPageSource().contains("Diamond")) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   }
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Diamond')]"));
	   String color = e.getCssValue("color");
	   assertEquals("rgba(255, 0, 0, 1)", color);
   }
   
   /*
    * Kiyan Zewer test 11/20
    * if the card is heart the text is red
    */
   @Test
   public void heartIsRed() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   while(!driver.getPageSource().contains("Heart")) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   }
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Heart')]"));
	   String color = e.getCssValue("color");
	   assertEquals("rgba(255, 0, 0, 1)", color);
   }
   
   /*
    * Kiyan Zewer test 12/20
    * if the card is a club the text is black
    */
   @Test
   public void clubIsBlack() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   while(!driver.getPageSource().contains("Clubs")) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   }
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Clubs')]"));
	   String color = e.getCssValue("color");
	   assertEquals("rgba(0, 0, 0, 1)", color);
   }
   
   /*
    * Kiyan Zewer test 13/20
    * if the card is a spade the text is black
    */
   @Test
   public void spadesIsBlack() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   while(!driver.getPageSource().contains("Spades")) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   }
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Spades')]"));
	   String color = e.getCssValue("color");
	   assertEquals("rgba(0, 0, 0, 1)", color);
   }
   
   /*
    * Kiyan Zewer test 14/20
    * result should be bust if player busts
    */
   @Test
   public void checkPlayerResultIsBust() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   while(playerTotal == 21 || driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText().contains("21")){
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   }
	   while(playerTotal < 21) {
		   driver.findElement(By.xpath("//button[text()='Hit']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
		   if(playerTotal == 21) {
			   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
			   e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
			   playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
		   }
	   }
	   assertTrue(driver.getPageSource().contains("Bust, you lose"));
   }
   
   /*
    * Kiyan Zewer test 15/20
    * result should be dealer busts if the dealer busts
    */
   @Test
   public void checkDealerResultIsBust() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   String dealer = e.getText();
	   while(playerTotal == 21 || dealer.contains("21")){
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   dealer = e.getText();
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   }
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   int dealerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   while(dealerTotal < 22) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   dealerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   }
	   assertTrue(driver.getPageSource().contains("Dealer Busts, you win"));
   }
   
   /*
    * Kiyan Zewer test 16/20
    * result should be push if it is a tie
    */
   @Test
   public void testPush() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   int dealerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   while(dealerTotal != playerTotal) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   dealerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   }
	   assertTrue(driver.getPageSource().contains("Push"));
   }
   
   
   /*
    * Kiyan Zewer test 17/20
    * result should be player wins if stand and has value larger than dealer without busting
    */
   @Test
   public void testWinWithoutDealerBust() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   String dealer = e.getText().substring(e.getText().indexOf("-") + 2);
	   int playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   int dealerTotal = Integer.parseInt(dealer);
	   while(dealerTotal >= 21 || playerTotal >= 21 || dealerTotal >= playerTotal && dealerTotal <= 21) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   dealerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
		   playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   }
	   assertTrue(driver.getPageSource().contains("You Win"));
   }
   
   /*
    * Kiyan Zewer test 18/20
    * result should be player loses if they stand and they have a value smaller 
    * than the dealer without busting
    */
   @Test
   public void testLoseWithoutDealerBust() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   String dealer = e.getText().substring(e.getText().indexOf("-") + 2);
	   int playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   int dealerTotal = Integer.parseInt(dealer);
	   while(dealerTotal >= 21 || playerTotal >= 21 || dealerTotal <= playerTotal && dealerTotal <= 21) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   dealerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
		   playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   }
	   assertTrue(driver.getPageSource().contains("You Lose"));
   }
   
   /*
    * Kiyan Zewer test 19/20
    * Player hits without busting and wins
    */
   @Test
   public void testHitNoBustWin() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Hit']")).click();
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   String dealer = e.getText().substring(e.getText().indexOf("-") + 2);
	   int playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   int dealerTotal = Integer.parseInt(dealer);
	   while(dealerTotal >= 21 || playerTotal <= dealerTotal || playerTotal >= 21) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Hit']")).click();
		   driver.findElement(By.xpath("//button[text()='Stand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   dealer = e.getText().substring(e.getText().indexOf("-") + 2);
		   dealerTotal = Integer.parseInt(dealer);
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   }
	   assertTrue(driver.getPageSource().contains("You Win"));
   }
   
   /*
    * Kiyan Zewer test 20/20
    * dealer total should be hidden unless player stands or game is over
    */
   @Test
   public void testDealerHiddenUntilStand() {
	   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
	   driver.findElement(By.xpath("//button[text()='Hit']")).click();
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   int playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   while(playerTotal > 21) {
		   driver.findElement(By.xpath("//button[text()='Place Bet']")).click();
		   driver.findElement(By.xpath("//button[text()='Hit']")).click();
		   f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
		   playerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   }
	   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   assertTrue(e.getText().contains("HIDDEN"));
   }
}   