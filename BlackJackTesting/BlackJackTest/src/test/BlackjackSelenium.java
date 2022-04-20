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
   private String url = "https://cs3250final.herokuapp.com/";
   
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
    * The Title should be '- Blackjack'
    */
   @Test
   public void testTitle() {
	   assertEquals(driver.getTitle(), "- Blackjack");
   }
   
   
   /*
    * This tests the log in feature
    */
//   @Test
//   public void testLogIn() {
//	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'log in')]"));
//	   e.click();
//	   WebElement f = driver.findElement(By.name("username"));
//	   f.sendKeys("kiyan");
//	   WebElement g = driver.findElement(By.name("password"));
//	   g.sendKeys("zewer");
//	   driver.findElement(By.name("login")).click();
//	   WebElement h = driver.findElement(By.xpath("//*[contains(text(), 'kiyan')]"));
//	   assertEquals("kiyan",h.getText());
//   }
   
   /*
    * New Hand button has 'New Hand' as text
    */
   @Test 
   public void testNewHand() {
	   assertEquals(driver.findElement(By.xpath("//button[text()='New Hand']")).getText(),"New Hand");
   }
   
   /*
    * Clicking New Hand should result in a player total less than 22 
    * as 21 is the largest possible starting hand
    */
   @Test
   public void testResetPlayerTotal() {
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   int bound = 22;
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   assertTrue(bound > playerTotal);
   }
   
   /*
    * Clicking hit as the first move should always result in a new player total value
    */
   @Test
   public void testHit() {
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   while(e.getText().equals("Player Total - 21") && f.getText().equals("Dealer Total - 21")) {
		   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
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
    * Clicking stand should change the Dealer Hand from hidden to shown
    */
   @Test
   public void testStandShowsText() {
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   String dealer = e.getText();
	   String player = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   while(dealer.equals("Dealer Total - 21") || player.equals("Player Total - 21")){
		   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
		   e = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
		   dealer = e.getText();
		   player = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   }
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   assertFalse(f.getText().contains("HIDDEN"));
   }
   
   /*
    * Clicking the stand button should keep the same Player Total value
    */
   @Test
   public void testStandKeepsSameValue() {
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   driver.findElement(By.xpath("//button[text()='Stand']")).click();
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   int newPlayerTotal = Integer.parseInt(f.getText().substring(f.getText().indexOf("-") + 2));
	   assertEquals(playerTotal, newPlayerTotal);
   }
   
   /*
    * If the dealer has blackjack the game should show their cards and show the total as 21 not hidden
    */
   @Test
   public void dealerBlackJack(){
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   String dealerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText();
	   while(dealerTotal.contains("HIDDEN")){
		   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
		   dealerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText();
	   }
	   assertEquals(dealerTotal, "Dealer Total - 21");
   }
   
   /*
    * If the player has blackjack the game should show their total as 21
    */
   @Test
   public void playerBlackJack(){
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   String playerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   while(!playerTotal.contains("21")){
		   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
		   playerTotal = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]")).getText();
	   }
	   assertEquals(playerTotal, "Player Total - 21");
   }
   
   /*
    * If the player clicks stand then the hit button should have no function
    * therefore, their total will be the same after clicking hit
    */
   @Test
   public void standThenHit() {
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   while(driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]")).getText().equals("Dealer Total - 21")) {
		   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
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
    * Clicking hit after you bust will not do anything as the game is over
    */
   @Test
   public void hitUntilBust() {
	   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
	   WebElement e = driver.findElement(By.xpath("//*[contains(text(), 'Player Total')]"));
	   WebElement f = driver.findElement(By.xpath("//*[contains(text(), 'Dealer Total')]"));
	   int playerTotal = Integer.parseInt(e.getText().substring(e.getText().indexOf("-") + 2));
	   while(f.getText().equals("Dealer Total - 21") || playerTotal == 21) {
		   driver.findElement(By.xpath("//button[text()='New Hand']")).click();
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
   
}   