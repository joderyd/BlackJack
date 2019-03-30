import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.TimerTask;
import java.util.concurrent.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;



class cardButton extends JButton{
	cardButton(){
		super();
		setBackground(new Color(0, 140, 0));
	}
	cardButton(String name){
		this(); 
		setText(name);
	}
}


class BetChanger extends JPanel implements ActionListener{
	private int currentBet, moneyLeft;
	private JButton plus = new JButton("+ 5");
	private JButton minus = new JButton("- 5");
	//private JTextField thisBet = new JTextField();
	private JLabel thisBet = new JLabel();
	private BlackJackBoard BJboard;
	protected BetChanger(BlackJackBoard gameBoard){
		super();
		setBackground(new Color(0, 165, 0));
		currentBet = 0;
		BJboard = gameBoard;
		moneyLeft = BJboard.getMoneyLeft();
		thisBet.setBackground(new Color(0, 165, 0));
		plus.setBackground(new Color(0,210,0));
		minus.setBackground(new Color(0,210,0));
		thisBet.setBackground(new Color(0,153,0));
		Font font = new Font("Helvetica", Font.BOLD, 20);
		thisBet.setFont(font);
		thisBet.setText(Integer.toString(currentBet) + " kr");
		thisBet.setHorizontalAlignment(SwingConstants.CENTER);
		setLayout(new GridLayout(3,0));
		plus.addActionListener(this);
		minus.addActionListener(this);
		add(plus);
		add(thisBet);
		add(minus);
	}
	public void resetBet() { 
		int initialBet = BJboard.getCurrentBet();
		currentBet=0;
		BJboard.addMoney(initialBet);
		BJboard.updateBalance();
		thisBet.setText("\n\n"+Integer.toString(currentBet) + " kr");
		
	}
	public void actionPerformed(ActionEvent e) {
		JButton b = ((JButton) e.getSource());
		moneyLeft = BJboard.getMoneyLeft();
		if(b==plus && moneyLeft >4 && BJboard.dealState() == "DEAL" && currentBet<50) {
			currentBet = currentBet+5;
			moneyLeft = moneyLeft-5;
			BJboard.removeMoney(5);
		}
		if(b==minus && currentBet >4 && BJboard.dealState() == "DEAL") {
			currentBet = currentBet-5;
			moneyLeft = moneyLeft+5;
			BJboard.removeMoney(-5);
		}
		BJboard.setCurrentBet(currentBet);
		thisBet.setText("\n\n"+Integer.toString(currentBet) + " kr");
		BJboard.updateBalance();
	}
}



class Bet extends JPanel{
	private BlackJackBoard gameBoard;
	private BetChanger betChanger;
	private JPanel betOnBoard;
	Bet(BlackJackBoard gameBoard){
		super();
		this.gameBoard = gameBoard;
		setLayout(new GridLayout(0, 2));
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new GridLayout(3,0));
		firstPanel.setBackground(new Color(0,165,0));
			//Everything that's in the firstPanel
			JPanel background1 = new JPanel();
			JPanel background2 = new JPanel();
			background1.setBackground(new Color(0, 165, 0));
			background2.setBackground(new Color(0, 165, 0));
			JLabel textPane = new JLabel();
			textPane.setBackground(new Color(0, 153, 0));
			textPane.setFont(new Font("Helvetica", Font.BOLD, 17));
			textPane.setText("Current bet: ");
				textPane.setHorizontalAlignment(SwingConstants.CENTER);
				textPane.setVerticalAlignment(SwingConstants.CENTER);
			firstPanel.add(background1);
			firstPanel.add(textPane);
			firstPanel.add(background2);
		betChanger = new BetChanger(gameBoard);
		add(firstPanel);
		add(betChanger);
		
	}
	public void update() { betChanger.resetBet(); }
}




class HitDeal extends cardButton{
	private String str1 = "DEAL";
	private String str2 = "HIT";
	private String str3 = "NEW BET";
	private String state = str1;
	private Color c1 = Color.yellow;
	private Color c2 = new Color(180, 250, 30);
	private Color c3 = Color.white;
	protected HitDeal(){
		super("DEAL");
		//setBorder(BorderFactory.createLineBorder(new Color(175, 255, 40), 4, true));
		setBorder(BorderFactory.createLineBorder(c1, 7, true));
		setBackground(Color.yellow);
		setFont(new Font("Helvetica", Font.BOLD, 22));
	}
	protected void toggle() {
		if(state == str1) {
			setBackground(c2);
			setText(str2);
			state = str2;
		}
		else if(state == str2) {
			setBackground(c3);
			setText(str3);
			state = str3;
		}
		else if(state == str3){
			setBackground(c1);
			setText(str1);
			state = str1;
		}
	}
	protected String getState() { return state; }
	
	public void endGame() { setBackground(Color.WHITE);
	setText("Game Finished");
	state = "End";
	}
}



class BetDisplay extends JPanel {
	JLabel text = new JLabel();
	JLabel betChips = new JLabel();
	private int betOnTable;
	BetDisplay(){
		text.setFont(new Font("Helvetica", Font.BOLD, 22));
		setLayout(new GridLayout(0, 2));
		setBackground(new Color(0, 165, 0));
			text.setBackground(new Color(0, 165, 0));
			betChips.setBackground(new Color(0, 165, 0));
		add(betChips);
		add(text);
	}
	public void newBet(int placedBet) {
		betOnTable = placedBet;
		betChips.setIcon(new ImageIcon("src/icons/chipsOnTable.png"));
		text.setText("Current Bet: " + placedBet + " kr");
	}
	public void hide() {
		betChips.setIcon(null);
		text.setText("");
	}
	
	public void win() {
		text.setText("You won " + betOnTable + " kr");
	}
	
	public void gameOver() {
		text.setFont(new Font("Helvetica", Font.BOLD, 22));
		text.setText("Game Over!");
	}
}


public class BlackJackBoard extends JFrame implements ActionListener{
	//private cardButton[] dealerHand = new cardButton[8]; // Placeholder for cards
	//private cardButton[] playerHand = new cardButton[8]; // Placeholder for cards
	private int totalMoneyBet, finalTime;
	private float totalAverage;
	private JLabel[] dealerHand = new JLabel[8];
	private JLabel[] playerHand = new JLabel[8];
	private Card[] dealerCards, playerCards; //Cards that have been dealt on current round
	private Deck deck = new Deck();  // Deck of 52 cards. method gives rigged hands
	private int handsPlayed = 0; //Counts number of bet placed (hands player)
	private int playerIndex = 0; //Player cards comes in a list. playerIndex tells current list-index
	private int clock = 0;
	private int dealerSum, playerSum, moneyLeft, currentBet, dealerIndex;
	private JPanel first, second, third, fourth, fifth, rounds; //the frame is devided into 5 parts
	private cardButton stand;  // a JButton for "STAND"
	private HitDeal hitDeal;    // JButton for HIT-DEAL-NEW DEAL
	private JLabel currentBalance, roundsPlayed, roundsLeft, dSum, pSum;;  // Display for current balance (moneyleft)
	private boolean Frozen = false;
	private Bet bet;		   // manages the bets
	private BetDisplay betDisplay = new BetDisplay();
	private PrintStream outPrint;
	private boolean winningSound;
	private String email;
	
	public BlackJackBoard(String name, int startingMoney, String email, boolean winningSound) {
		super(name);
		moneyLeft = startingMoney;
		this.winningSound = winningSound;
		this.email = email;
		currentBet = 0;
		dealerIndex = 0;
		totalMoneyBet = 0;
		totalAverage = 0;
		currentBalance = new JLabel("Money Left: " + moneyLeft + " kr");
			currentBalance.setHorizontalAlignment(SwingConstants.CENTER);
			currentBalance.setFont(new Font("Helvetica", Font.BOLD, 22));
			currentBalance.setBorder(BorderFactory.createBevelBorder(1, new Color(0, 140,0), new Color(0, 90, 0)));
		Container container = getContentPane();
		setSize(1600,1000);
		container.setLayout(new GridLayout(5, 0));
		first = new JPanel(); second = new JPanel();
		third = new JPanel(); fourth = new JPanel(); 
		fifth = new JPanel();
			// Filling in the 5 parts (JPanels)
			first.setBackground(new Color(0,165,0));
			first.setLayout(new GridLayout(0, 9));
			second.setBackground(new Color(0,153,0));
			second.setLayout(new GridLayout(0, 12));
			third.setLayout(new GridLayout(1, 7));
			third.setBackground(new Color(0,165,0));
				JPanel sums = new JPanel();
				sums.setLayout(new GridLayout(2, 0));
				sums.setBackground(new Color(0,165,0));
					dSum = new JLabel("Dealer sum: " );
						dSum.setFont(new Font("Helvetica", Font.BOLD, 16)); 
						dSum.setHorizontalAlignment(SwingConstants.CENTER);
					pSum = new JLabel("Player sum: ");
						pSum.setFont(new Font("Helvetica", Font.BOLD, 16));
						pSum.setHorizontalAlignment(SwingConstants.CENTER);
				sums.add(dSum);
				sums.add(pSum);
			third.add(sums);
				JLabel firstFiller = new JLabel(); firstFiller.setBackground(new Color(0,165,0));
				JLabel secondFiller = new JLabel(); secondFiller.setBackground(new Color(0,165,0));
			third.add(betDisplay);
			third.add(firstFiller);
			third.add(secondFiller);
		fourth.setBackground(new Color(0,153,0));
		fourth.setLayout(new GridLayout(0, 12));
		fifth.setBackground(new Color(0,153,0));
		fifth.setLayout(new GridLayout(0,5));
			bet = new Bet(this);
			hitDeal = new HitDeal();
				hitDeal.addActionListener(this);
			stand = new cardButton("STAND");
			stand.setBorder(BorderFactory.createLineBorder(new Color(0, 95, 0), 4, true));
			stand.setFont(new Font("Helvetica", Font.BOLD, 22));
				stand.setBackground(new Color(0, 120, 0));
				stand.addActionListener(this);
			rounds = new JPanel();
			rounds.setBackground(new Color(0, 165, 0));
			rounds.setLayout(new GridLayout(2, 0));
				roundsPlayed = new JLabel("Hands Played: 0");
					roundsPlayed.setFont(new Font("Helvetica", Font.BOLD, 22));
				roundsLeft = new JLabel("Hands to play: 15");
					roundsLeft.setFont(new Font("Helvetica", Font.BOLD, 22));
				rounds.add(roundsPlayed);
				rounds.add(roundsLeft);
					roundsLeft.setHorizontalAlignment(SwingConstants.CENTER);
					roundsPlayed.setHorizontalAlignment(SwingConstants.CENTER);
			//fifth.add(bet);
			fifth.add(currentBalance);
			fifth.add(bet);
			fifth.add(hitDeal);
			fifth.add(stand);;
			fifth.add(rounds);
		for(int i=0; i<12; i++) {
			if(i<2 || i>9) {
				JPanel temp1 = new JPanel();
				temp1.setBackground(new Color(0, 165, 0));
				JPanel temp2 = new JPanel();
				temp2.setBackground(new Color(0, 165, 0));
				second.add(temp1);
				fourth.add(temp2);
			}
			else if(i>1 && i<10) {
				JLabel dealerCard = new JLabel();
					dealerCard.setBackground(new Color(0, 120, 0));
					dealerCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				JLabel playerCard = new JLabel();
					playerCard.setBackground(new Color(0, 120, 0));
					playerCard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				dealerHand[i-2] = dealerCard;
				playerHand[i-2] = playerCard;
				second.add(dealerCard);
				fourth.add(playerCard);
			}
			if(i<9) {
				JLabel background = new JLabel();
				background.setBackground(new Color(0, 165, 0));
				if(email == "DEMO") {
					background.setText("DEMO");
					background.setFont(new Font("Helvetica", Font.BOLD, 26));
				}
				first.add(background);
				if(i==4) { 
					ImageIcon firstIcon = new ImageIcon("src/icons/deck.jpg");
					Image img = firstIcon.getImage();
					Image imgNew = img.getScaledInstance(150, 200, java.awt.Image.SCALE_SMOOTH);
					background.setIcon(new ImageIcon(imgNew));
				} 
			}

		}
		dealerHand[0].setBorder(BorderFactory.createLineBorder(Color.yellow));
			dealerHand[0].setFont(new Font("Helvetica", Font.BOLD, 16));
			dealerHand[0].setText("Dealer");
			dealerHand[0].setHorizontalAlignment(SwingConstants.CENTER);
		playerHand[0].setBorder(BorderFactory.createLineBorder(Color.yellow));
			playerHand[0].setFont(new Font("Helvetica", Font.BOLD, 16));
			playerHand[0].setText("Player");
			playerHand[0].setHorizontalAlignment(SwingConstants.CENTER);
		
		container.add(first);
		container.add(second);
		container.add(third);
		container.add(fourth);
		container.add(fifth);
		try {
			
			outPrint = new PrintStream(new FileOutputStream(email + ".txt"));
			//printFile = new PrintWriter("result-file.txt", "UTF-8");
			String sound = "Off";
			if(winningSound) { sound = "On"; }
			outPrint.print("\r\nPlayer : " + email + "\r\nsound: " + sound + "\r\n\r\n");
			outPrint.flush();
			//writer.close();
		} catch(Throwable t) { System.out.println("Wrong with writing to textfile!");}
		playSound("shuffling1");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	public void endGame() {
		JOptionPane.showMessageDialog(this, "Game Over!");
		Frozen = true;
		outPrint.print("\r\n\r\n--------------FINAL RESULT : ---------------");
		outPrint.print("\r\n\r\nHands played: " + handsPlayed);
		outPrint.print("\r\nAverage bet size: " + totalMoneyBet/handsPlayed);
		outPrint.print("\r\nTime spent: " + clock + " seconds");
		outPrint.print("\r\nTime spent per hand: " + clock/handsPlayed + " seconds per round");
		outPrint.flush();
		outPrint.close();
		playSound("GameOver");
		hitDeal.endGame();
		//playSound("GameOver2");
	}
	
	public void incrementClock(int i) {	clock = clock +i; }
	public String dealState() { return hitDeal.getState(); }
	public int getMoneyLeft() {return moneyLeft;}
	public void removeMoney(int moneyToRemove) {moneyLeft = moneyLeft - moneyToRemove;}
	public int getCurrentBet() {return currentBet;}
	public void setCurrentBet(int bet) {currentBet = bet;}
	public void updateRounds() { roundsPlayed.setText("Hands Played: " + handsPlayed); }
	public JLabel getDealerButton(int i) { return dealerHand[i]; }
	public JLabel getPlayerButton(int i) { return playerHand[i]; }
	public JLabel getFaceDownCard() { return dealerHand[1]; }
	public Card getDealerCard(int i) { return dealerCards[i]; }
	public Card getPlayerCard(int i) { return playerCards[i]; }
	public int getPlayerIndex() { return playerIndex; }
	public void unfreeze() { Frozen = false; /* System.out.print("\n\nUnfrozen!\n\n"); */ }
	public void freeze() { Frozen = true; /* System.out.print("\n\nFrozen!\n\n"); */ }
	public int getDealerSum(int dI) {
		int dS = 0;
		for(int i = 0; i<dI; i++) { dS = dS + getDealerCard(i).getValue(); }
		return dS;
	}
	public int getPlayerSum(int pI) {
		int pS = 0;
		for(int i = 0; i<pI; i++) { pS = pS + getPlayerCard(i).getValue(); }
		return pS;
	}
	public void hitDealToggle() { hitDeal.toggle(); }
	public void addMoney(int money) { moneyLeft = moneyLeft + money; }
	public void updateBalance() {
		currentBalance.setText("Money Left: " + moneyLeft + " kr");
		roundsPlayed.setText("Hands Played: " + handsPlayed);
	}
	
	public void updateSums(int dealerIndex, int playerIndex) {
		updateDealerSum(dealerIndex);
		updatePlayerSum(playerIndex);
	}
	public void updatePlayerSum(int playerIndex) { 
		int pS = 0;
		for(int k =0; k<playerIndex; k++) { pS = pS + getPlayerCard(k).getValue(); }
		pSum.setText("\tPlayer sum: " + pS); 
	}
	public void updateDealerSum(int dealerIndex) { 
		int dS = 0;
		for(int i = 0; i<dealerIndex; i++) { dS = dS + getDealerCard(i).getValue(); }
		dSum.setText("\tDealer sum: " + dS); 
	}
	
	public void startClock() {
		ClockTimer cTimer = new ClockTimer(this);
		Timer timer = new Timer(1000, cTimer);
		timer.start();
	}
	
	private void reset() {
		
		betDisplay.hide();
		Frozen = false;
		for(int i=0; i<8; i++) {
			dealerHand[i].setIcon(null);
			playerHand[i].setIcon(null);
		}
		hitDeal.toggle();
		playerSum = 0;
		dealerSum = 0;
		currentBet = 0;
		updateBalance();
		playerIndex = 0;
		dealerIndex = 0;
		dSum.setText("Dealer sum: ");
		pSum.setText("Player sum: ");
		dealerHand[0].setBorder(BorderFactory.createLineBorder(Color.yellow));
		playerHand[0].setBorder(BorderFactory.createLineBorder(Color.yellow));
		dealerHand[0].setText("Dealer");
		playerHand[0].setText("Player");
		//playSound("shuffling");
		playSound("shuffling1");
	}
	
	
	
	// ACTIONPERFORMED //
	public void actionPerformed(ActionEvent e) {
		if(Frozen) { JOptionPane.showMessageDialog(this, "Wait until the cards have been dealt!"); }
		
		else if(!Frozen) {
		cardButton b = ((cardButton) e.getSource());
		if(b == hitDeal) {
			if(hitDeal.getState() == "DEAL") {
				if(currentBet < 5) {
					JOptionPane.showMessageDialog(this, "You must place a bet before the deal!");
				}
				else {
					//playSound("shuffling");
					int yesno = JOptionPane.showConfirmDialog(this, "Are you sure you want to bet  " 
							+ currentBet + "  kr on this hand?", "Confirm", JOptionPane.YES_NO_OPTION);
					if(yesno == JOptionPane.YES_OPTION) {
						if(handsPlayed == 0) { 
							startClock();
						}
						handsPlayed = handsPlayed + 1;
						((HitDeal) b).toggle();
						deal(handsPlayed);
					}
					else if(yesno == JOptionPane.NO_OPTION) { }
				}
			}
			else if(hitDeal.getState() == "HIT") { Hit(); }
			else if(hitDeal.getState() == "NEW BET") { reset(); }
		}
		if(b==stand && hitDeal.getState ()=="HIT") { Stand(); }
		}
	}
	
	
	//Methods that deals cards on the table with timers to create a delay of approx 700ms between cards being dealt.
	public void showFirstDealerCard() {
		try { 
			TimerD task = new TimerD(this, 0); 
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(task, 1300, TimeUnit.MILLISECONDS);
		} catch(Throwable t) { System.out.print("Fel i firstDealerCard");}
	}
	public void showSecondDealerCard() {
		try { 
			FaceDownDealerTimer task = new FaceDownDealerTimer(this); 
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(task, 2900, TimeUnit.MILLISECONDS);
		} catch(Throwable t) { System.out.print("Fel i secondDealerCard");}
	}
	
	
	public void showFirstPlayerCard() {
		try { 
			playerSum = playerCards[0].getValue();
			TimerP task = new TimerP(this, 0);
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(task, 500, TimeUnit.MILLISECONDS);
		} catch(Throwable t) { System.out.print("Fel i firstPlayerCard");}
	}
	public void showSecondPlayerCard() {
		try { 
			playerSum = playerCards[0].getValue() + playerCards[1].getValue();
			TimerP task = new TimerP(this, 1);
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(task, 2100, TimeUnit.MILLISECONDS); 
		} catch(Throwable t) { System.out.print("Fel i secondPlayerCard");}
	}
	
	
	
	public void showDealerCards(int i) {
		try { 
			ThreadDealer dTimer = new ThreadDealer(this, i);
			Timer timer = new Timer(800, dTimer);
			dTimer.addTimer(timer);
			playSound("FlipCard");
			timer.start();

		} 
		catch(Throwable t) { System.out.print("Fel i showDealerCards");}
	}
	

	
	public void deal(int handsPlayed) {
		totalMoneyBet = totalMoneyBet + currentBet;
		totalAverage = totalAverage + currentBet/(moneyLeft+currentBet);
		if(handsPlayed>15) {
			JOptionPane.showMessageDialog(this, "You have played all 15 rounds!");
		}
		else {
		freeze();
		betDisplay.newBet(currentBet);
		String info = "\r\n\r\nHand number: " + handsPlayed + "\r\n  Bet size: "+ currentBet +" kr" +"\r\n  Money left: "
				+ moneyLeft + " kr\r\n Seconds passed since start: " + clock + "\r\n player moves: ";
		System.out.print(info);
		outPrint.print(info);
		outPrint.flush();
		playSound("betting");
		
		playerIndex = 0;
		//handsPlayed = handsPlayed +1;
		updateRounds();
		if(email == "DEMO") {
			dealerCards = deck.getRandomHand();
			playerCards = deck.getRandomHand();
		}
		else {
			dealerCards = deck.getDealerHand(handsPlayed);
			playerCards = deck.getPlayerHand(handsPlayed);
		}
		playerIndex = 2;
		dealerIndex = 1;
		showFirstDealerCard();
		showSecondDealerCard();
		dealerSum = 0;
		showFirstPlayerCard();
		showSecondPlayerCard();
	}
		if (handsPlayed == 4) {
			try { 
				winBJTimer task = new winBJTimer(this); 
				ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
				executor.schedule(task, 2900, TimeUnit.MILLISECONDS);
				//unfreeze();
				System.out.println("\nPlayer won");
				outPrint.print("\r\nPlayer won\r\n");
			}
			catch(Throwable t) { System.out.print("Something wrong in Dealer-method (blackjack-hand nr 4)"); }
		}
	}
	
	
	public void Hit() {
		playSound("FlipCard");
		System.out.print("  Hit  ");
		outPrint.print("Hit, ");
		playerHand[playerIndex].setIcon(playerCards[playerIndex].getIcon());
		playerIndex = playerIndex + 1;
		playerSum = getPlayerSum(playerIndex);
		updatePlayerSum(playerIndex);
		if(playerSum< 22) {	 /* do something? */}
		else if (playerSum > 21) { playerLoses(); }
	}
	
	
	public void Stand() {
		System.out.print("  STAND  ");
		outPrint.print("Stand, ");
		try {
			freeze();
			Thread.sleep(500);
			playSound("FlipCard");
			dealerHand[1].setIcon(dealerCards[1].getIcon());
			dealerSum = dealerCards[0].getValue() + dealerCards[1].getValue();
			updateDealerSum(2);
			showDealerCards(dealerIndex+1);
		} catch(Throwable t) {}
	}
	
	
	public void playerWins() {
		moneyLeft = moneyLeft + currentBet;
		updateBalance();
		bet.update();
		if(winningSound) {
			if(handsPlayed == 4) {
				playSound("BlackJack");	
			}
			else {
				playSound("easyMoney"); playSound("Coins");
			}
		}
		betDisplay.win();
		try { 
			WinningPane task = new WinningPane(this, handsPlayed); 
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(task, 400, TimeUnit.MILLISECONDS);
			//unfreeze();
			System.out.println("\nPlayer won");
			outPrint.print("\r\nPlayer won\r\n");
		}
		catch(Throwable t) { System.out.print("Something wrong in playerWins()-method"); }
		finally {
			if(handsPlayed>14) {
				endGame();
			}
		}
	}
	
	
	public void playerLoses() {
		if(moneyLeft<5 || handsPlayed>14) { 
			System.out.print("Player lost\r\n");
			System.out.print("\n\nMONEY LEFT: " + moneyLeft);
			outPrint.print("\r\n\r\n\r\nGame finished!\r\n");
			endGame();
		}
		moneyLeft = moneyLeft - currentBet;
		try {
			Thread.sleep(400);
		}catch(Throwable t) {System.out.print("Something wrong in playerLoses()");}
		unfreeze();
		updateBalance();
		bet.update();
		String currentString = "You lost!\n\n";
		if(playerSum>21) {
			currentString = currentString + "You reached the sum: " + playerSum;
		}
		JOptionPane.showMessageDialog(this, currentString);
		hitDeal.toggle();
		System.out.println("\nPlayer lost");
		outPrint.print("\r\nPlayer lost\r\n");
	}
	
	
	public void tieResult() {
		if(moneyLeft<5 || handsPlayed>14) { 
			System.out.print("\n\nMONEY LEFT: " + moneyLeft);
			outPrint.print("\r\nTie!\r\n");
			endGame();
		}
		moneyLeft = moneyLeft;
		try {
			Thread.sleep(400);
		}catch(Throwable t) {System.out.print("Something wrong in tieResult()");}
		unfreeze();
		updateBalance();
		bet.update();
		String currentString = "Tie!\r\n You win back your last bet";
		JOptionPane.showMessageDialog(this, currentString);
		hitDeal.toggle();
		System.out.println("\nTie");
	
	}
	
	// PLAY A SOUND CLIP (Victory)
	public static void playSound(String winlose){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/sounds/" + winlose + ".wav"));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
        }
        catch(Throwable t){ System.out.println("Error with playing sound."); }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// MAIN METHOD
	public static void main(String[] args) {
		//new BlackJackBoard("Black Jack", 50, "oderyd", true);   // false -> inget ljud vid vinst
		//new BlackJackBoard("Black Jack", 50, "DEMO", true);	//  "DEMO" ger nästan randomizade händer
	}
}
























class FaceDownDealerTimer extends TimerTask{
	BlackJackBoard BJboard;
	FaceDownDealerTimer(BlackJackBoard BJboard) { 
		super();
		this.BJboard = BJboard; 
	}
	public void run() {
		JLabel faceDown = BJboard.getFaceDownCard();
		ImageIcon firstIcon = new ImageIcon("src/icons/facedown.png");
		Image img = firstIcon.getImage();
		Image imgNew = img.getScaledInstance(140, 200, java.awt.Image.SCALE_SMOOTH);
		BJboard.playSound("FlipCard");
		faceDown.setIcon(new ImageIcon(imgNew));
	}
	
}


class TimerD extends TimerTask{
	BlackJackBoard BJb;
	int index;
	TimerD(BlackJackBoard BJb, int index){ 
		super(); 
		this.BJb = BJb;
		this.index = index;
	}
	public void run() {
		JLabel next = BJb.getDealerButton(index);
		if(next!=null) {
			BJb.getDealerButton(0).setText(null);
			BJb.getDealerButton(0).setBorder(null);
			BJb.playSound("FlipCard");
			next.setIcon(BJb.getDealerCard(index).getIcon());
			BJb.updateDealerSum(index+1);
		}
	}
}

class UnfreezeTimer extends TimerTask {
	BlackJackBoard BJb;
	UnfreezeTimer(BlackJackBoard BJb){
		super();
		this.BJb = BJb;
	}
	public void run() {
		BJb.unfreeze();
	}
}

class TimerP extends TimerTask{
	BlackJackBoard BJb;
	int index;
	TimerP(BlackJackBoard BJb, int index){ 
		super(); 
		this.BJb = BJb;
		this.index = index;
	}
	public void run() {
		JLabel next = BJb.getPlayerButton(index);
		if(next!=null) {
			BJb.getPlayerButton(0).setText(null);
			BJb.getPlayerButton(0).setBorder(null);
			BJb.playSound("FlipCard");
			next.setIcon(BJb.getPlayerCard(index).getIcon());
			BJb.updatePlayerSum(index+1);
		}
		if(index == 1) { BJb.unfreeze(); }
	}
}



class ThreadPlayer implements ActionListener {
	BlackJackBoard BJboard;
	int i;
	ThreadPlayer(BlackJackBoard BJboard, int index){ 
		this.BJboard = BJboard; 
		i = index;
	}
	public void actionPerformed(ActionEvent e) {
		if(BJboard.dealState() != "DEAL") {
			JLabel next = BJboard.getPlayerButton(i);
			next.setIcon(BJboard.getPlayerCard(i).getIcon());
			BJboard.playSound("FlipCard");
			if(i!=0) {
				BJboard.updatePlayerSum(i+1);
			}
			else { BJboard.updatePlayerSum(i); }
		}
	}
}

class ThreadDealer implements ActionListener {
	BlackJackBoard BJboard;
	int i;
	Timer timer;
	ThreadDealer(BlackJackBoard BJboard, int index){ 
		this.BJboard = BJboard; 
		i = index;
	}
	public void addTimer(Timer timer) { this.timer=timer; }

	public void actionPerformed(ActionEvent e) {
		int dSum = BJboard.getDealerSum(i);
		int pI = BJboard.getPlayerIndex();
		int pSum = BJboard.getPlayerSum(pI);
		if( dSum>16 && dSum<22 && pSum<dSum) {
			timer.stop();
			BJboard.playerLoses();
		}
		else if( (dSum > 16 && pSum>dSum) || dSum>21) {
			timer.stop();
			BJboard.playerWins();
		}
		
		else if( (dSum > 16 && pSum==dSum) || dSum>21) {
			timer.stop();
			BJboard.tieResult();
		}
		else if(BJboard.dealState() != "DEAL" && BJboard.getDealerSum(i)<17) {
			BJboard.playSound("FlipCard");
			JLabel next = BJboard.getDealerButton(i);
			next.setIcon(BJboard.getDealerCard(i).getIcon());
			if(i!=0) {
				BJboard.updateDealerSum(i+1);
			}
			else { BJboard.updateDealerSum(i); }
			i = i+1;
		}
	}
}


class ClockTimer implements ActionListener {
	BlackJackBoard BJboard;
	int i;
	ClockTimer(BlackJackBoard BJboard){ 
		this.BJboard = BJboard; 
	}
	public void actionPerformed(ActionEvent e) { BJboard.incrementClock(1); }
}


class winBJTimer extends TimerTask{
	private BlackJackBoard BJb;
	protected winBJTimer(BlackJackBoard BJb){
		super(); 
		this.BJb = BJb;
	}
	public void run() {
		BJb.Stand();
	}
}

class WinningPane extends TimerTask{
	private BlackJackBoard BJb;
	private int round;
	protected WinningPane(BlackJackBoard BJb, int round){
		super(); 
		this.BJb = BJb;
		this.round = round;
	}
	public void run() {
		String message = "You Win!\n\nYou won +" + BJb.getCurrentBet();
		if(round == 4) {
			message = "BLACK JACK!\r\n\r\n Congratulations! \r\n\r\n You win +" + BJb.getCurrentBet();
		}
		JOptionPane.showMessageDialog(BJb, message);
		BJb.hitDealToggle();
		BJb.unfreeze();
	}
}