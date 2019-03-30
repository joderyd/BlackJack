import java.util.Random;

public class Deck {
	Card[] cards = new Card[53];
	Deck(){
		cards[0] = new Card("Joker", "JOKER", 0);
	
		cards[1] = new Card("Clubs", "2", 2);
		cards[2] = new Card("Hearts", "2", 2);
		cards[3] = new Card("Spades", "2", 2);
		cards[4] = new Card("Diamonds", "2", 2);
		cards[5] = new Card("Clubs", "3", 3);
		cards[6] = new Card("Hearts", "3", 3);
		cards[7] = new Card("Spades", "3", 3);
		cards[8] = new Card("Diamonds", "3", 3);
		cards[9] = new Card("Clubs", "4", 4);
		cards[10] = new Card("Hearts", "4", 4);
		cards[11] = new Card("Spades", "4", 4);
		cards[12] = new Card("Diamonds", "4", 4);
		cards[13] = new Card("Clubs", "5", 5);
		cards[14] = new Card("Hearts", "5", 5);
		cards[15] = new Card("Spades", "5", 5);
		cards[16] = new Card("Diamonds", "5", 5);
		cards[17] = new Card("Clubs", "6", 6);
		cards[18] = new Card("Hearts", "6", 6);
		cards[19] = new Card("Spades", "6", 6);
		cards[20] = new Card("Diamonds", "6", 6);
		cards[21] = new Card("Clubs", "7", 7);
		cards[22] = new Card("Hearts", "7", 7);
		cards[23] = new Card("Spades", "7", 7);
		cards[24] = new Card("Diamonds", "7", 7);
		cards[25] = new Card("Clubs", "8", 8);
		cards[26] = new Card("Hearts", "8", 8);
		cards[27] = new Card("Spades", "8", 8);
		cards[28] = new Card("Diamonds", "8", 8);
		cards[29] = new Card("Clubs", "9", 9);
		cards[30] = new Card("Hearts", "9", 9);
		cards[31] = new Card("Spades", "9", 9);
		cards[32] = new Card("Diamonds", "9", 9);
		cards[33] = new Card("Clubs", "10", 10);
		cards[34] = new Card("Hearts", "10", 10);
		cards[35] = new Card("Spades", "10", 10);
		cards[36] = new Card("Diamonds", "10", 10);
		cards[37] = new Card("Clubs", "J", 10);
		cards[38] = new Card("Hearts", "J", 10);
		cards[39] = new Card("Spades", "J", 10);
		cards[40] = new Card("Diamonds", "J", 10);
		cards[41] = new Card("Clubs", "Q", 10);
		cards[42] = new Card("Hearts", "Q", 10);
		cards[43] = new Card("Spades", "Q", 10);
		cards[44] = new Card("Diamonds", "Q", 10);
		cards[45] = new Card("Clubs", "K", 10);
		cards[46] = new Card("Hearts", "K", 10);
		cards[47] = new Card("Spades", "K", 10);
		cards[48] = new Card("Diamonds", "K", 10);
		cards[49] = new Card("Clubs", "A", 11);
		cards[50] = new Card("Hearts", "A", 11);
		cards[51] = new Card("Spades", "A", 1);
		cards[52] = new Card("Diamonds", "A", 1);
		
	}
	public Card[] getDealerHand(int round) {
		Card[] result = new Card[8];
		// Easy win
		if(round == 1) { result[0] = cards[26]; result[1] = cards[27]; result[2] = cards[43]; }
		
		// Possible
		if(round == 2) { result[0] = cards[6]; result[1] = cards[37]; result[2] = cards[21]; result[3] = cards[39]; }
		// Impossible
		if(round == 3) { result[0] = cards[22]; result[1] = cards[5]; result[2] = cards[13]; result[3] = cards[11]; result[4] = cards[44];}
		//Black Jack
		if(round == 4) { result[0] = cards[41]; result[1] = cards[29]; }
		// Hard
		if(round == 5) { result[0] = cards[48]; result[1] = cards[29]; result[2] = cards[14];}
		// Impossible
		if(round == 6) { result[0] = cards[6]; result[1] = cards[9]; result[2] = cards[35];}
		//Easy
		if(round == 7) { result[0] = cards[27]; result[1] = cards[26]; result[2] = cards[20];}
		//Possible
		if(round == 8) { result[0] = cards[9]; result[1] = cards[22]; result[2] = cards[51]; result[3] = cards[37];}
		// Impossible (E5)
		if(round == 9) { result[0] = cards[2]; result[1] = cards[11]; result[2] = cards[29]; result[3] = cards[3];}
		// Hard (H6)
		if(round == 10) { result[0] = cards[42]; result[1] = cards[40];}
		//Easy (E7)
		if(round == 11) { result[0] = cards[24]; result[1] = cards[13]; result[2] = cards[35];}
		// Possible (P8)
		if(round == 12) { result[0] = cards[15]; result[1] = cards[25]; result[2] = cards[14];}
		// Impossible (I9)
		if(round == 13) { result[0] = cards[46]; result[1] = cards[49];}
		//Hard (H10)
		if(round == 14) { result[0] = cards[35]; result[1] = cards[9]; result[2] = cards[2]; result[3] = cards[44];}
		// Possible (P11)
		if(round == 15) { result[0] = cards[42]; result[1] = cards[40];}
		return result;
	}
	
	public Card[] getPlayerHand(int round) {
		Card[] result = new Card[8];
		//Easy win
		if(round == 1) { result[0] = cards[48]; result[1] = cards[29]; result[2] = cards[1]; result[3] = cards[15];}
		//possible
		if(round == 2) { result[0] = cards[16]; result[1] = cards[10]; result[2] = cards[25]; result[3] = cards[7]; result[4] = cards[48];}
		//Impossible
		if(round == 3) { result[0] = cards[1]; result[1] = cards[37] ; result[2] = cards[12]; result[3] = cards[52]; result[4] = cards[45]; }
		// Black Jack
		if(round == 4) { result[0] = cards[49]; result[1] = cards[45]; result[2] = cards[5];}
		// Hard
		if(round == 5) { result[0] = cards[18]; result[1] = cards[23]; result[2] = cards[15]; result[3] = cards[1]; result[4] = cards[42];}	
		// Impossible
		if(round == 6) { result[0] = cards[32]; result[1] = cards[3]; result[2] = cards[10]; result[3] = cards[36];}
		//Easy
		if(round == 7) { result[0] = cards[43]; result[1] = cards[40]; result[2] = cards[16];}
		//Possible
		if(round == 8) { result[0] = cards[47]; result[1] = cards[19]; result[2] = cards[28];}
		
		// Easy (E5)
		if(round == 9) { result[0] = cards[26]; result[1] = cards[40]; result[2] = cards[2]; result[3] = cards[41];}
		// Hard (H6)
		if(round == 10) { result[0] = cards[5]; result[1] = cards[18]; result[2] = cards[30]; result[3] = cards[7]; result[4] = cards[42];}
		//Easy (E7)
		if(round == 11) { result[0] = cards[47]; result[1] = cards[38]; result[2] = cards[7];}
		// Possible (P8)
		if(round == 12) { result[0] = cards[48]; result[1] = cards[19]; result[2] = cards[6]; result[3] = cards[28];}
		// Impossible (I9)
		if(round == 13) { result[0] = cards[34]; result[1] = cards[30]; result[2] = cards[51]; result[3] = cards[23];}
		//Hard (H10)
		if(round == 14) { result[0] = cards[17]; result[1] = cards[27]; result[2] = cards[40];}
		// Possible (P11)
		if(round == 15) { result[0] = cards[6]; result[1] = cards[23]; result[2] = cards[20]; result[3] = cards[3]; result[4] = cards[29];}
		
		return result;
	}
	
	
	
	public Card[] getRandomHand() {
		boolean[] memory = new boolean[52];
		for(int i = 0; i<51; i++) {
			memory[i] = false;
		}
		Card[] result = new Card[8];
		Random r = new Random();
		int low = 1; int high = 51;
		int handIndex = 0;
		for(int j = 0; j<7; j++) {
			int rI = r.nextInt(high-low)+low;
			result[j] = cards[rI];
		}
		return result;
	}
	
}
