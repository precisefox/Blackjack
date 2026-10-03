import java.util.*;
public class Blackjack {

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		final String ANSI_GREEN = "\u001B[38;2;128;255;128m";
		final String ANSI_RED = "\u001B[38;2;255;128;128m";
		final String ANSI_RESET = "\u001B[0m";
		final String ANSI_SKY = "\u001B[38;2;128;191;255m";
		final String ANSI_SILVER = "\u001B[38;2;191;191;191m";
		
		System.out.println(ANSI_SILVER + "****************************************************************************************************");
		System.out.println(ANSI_RESET + "Welcome to Blackjack!");
		System.out.println(ANSI_SILVER + "****************************************************************************************************");
		
		int gameNum = 1;
		int money = 1000;
		int bet = 0;
		boolean gameActive = true;
		while (gameActive) {
			if (money <= 0) {
				System.out.println(ANSI_RED + "Bankrupt!\nGAME OVER");
				gameActive = false;
			} else {
				System.out.println(ANSI_GREEN + "Your money: " + money + "$");
				boolean waitingForInput = true;
				while (waitingForInput) {
					System.out.print(ANSI_RESET + "Enter your bet: " + ANSI_SKY);
					bet = input.nextInt();
					if (bet > money) {
						System.out.println(ANSI_RED + "Error: You cannot bet more money than you have!");
					} else if (bet <= 0) {
						System.out.println(ANSI_RED + "Error: You cannot bet that little money!");
					} else {
						waitingForInput = false;
					}
				}
				int winnings = blackjack(bet, gameNum);
				System.out.println(winnings + "$)\n");
				money += winnings;
				gameNum++;
			}
		}
		input.close();

	}
	
	public static int blackjack(int bet, int gameNum) {
		
		// colors
		final String ANSI_RESET = "\u001B[0m";
		final String ANSI_SILVER = "\u001B[38;2;191;191;191m";
		final String ANSI_SKY = "\u001B[38;2;128;191;255m";
		final String ANSI_RED = "\u001B[38;2;255;128;128m";
		final String ANSI_GREEN = "\u001B[38;2;128;255;128m";
		final String ANSI_GOLD = "\u001B[38;2;255;223;128m";
		
		Scanner input = new Scanner(System.in);
		boolean gameActive = true;
		boolean waitingForInput = true;
		int userAction = 0;
		int[] dealerHand = {-1, -1, -1, -1, -1};
		int[] playerHand = {-1, -1, -1, -1, -1};
		dealerHand[0] = drawCard();
		dealerHand[1] = drawCard();
		playerHand[0] = drawCard();
		playerHand[1] = drawCard();
		System.out.println(ANSI_RESET + "Game " + gameNum + ": " + ANSI_GREEN + "(" + bet + "$)");
		while (gameActive) {
			System.out.println(ANSI_SILVER + "Dealer:");
			convertCard(dealerHand[0]);
			for (int i = 1; i <= 4; i++) {
				if (dealerHand[i] != -1) {
					System.out.print(ANSI_SKY + "[??]");
				}
			}
			System.out.println();
			System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + "?\n");
			System.out.println(ANSI_SILVER + "You:");
			for (int i = 0; i <= 4; i++) {
				if (playerHand[i] != -1) {
					convertCard(playerHand[i]);
				}
			}
			System.out.println();
			if (checkAce(playerHand)) {
				if (calculateValue(playerHand) <= 11) {
					System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "/" + (calculateValue(playerHand) + 10) + "\n");
				} else {
					System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
				}
			} else {
				System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
			}
			waitingForInput = true;
		
			while (waitingForInput) {
				System.out.println(ANSI_SILVER + "Input [1] to hit!");
				System.out.println(ANSI_SILVER + "Input [2] to stand!");
				System.out.println(ANSI_SILVER + "Input [3] to double down!");
				System.out.print("\n" + ANSI_RESET + "Enter input here: " + ANSI_SKY);
				userAction = input.nextInt();
				if (userAction > 3) {
					System.out.println(ANSI_RED + "Invalid input!\n");
				} else if (userAction > 0 && userAction < 4) {
					waitingForInput = false;
				} else {
					System.out.println(ANSI_RED + "Invalid input!\n");
				}
				
			}
		
			switch (userAction) {
				case 1:
					int increment = 0;
					while ((playerHand[increment] != -1) && (increment < 5)) {
						increment++;
					}
					
					if (increment < 4) {
						playerHand[increment] = drawCard();
						if (resolvePlayerBust(playerHand, bet)) {
							return -bet;
						}
					} else {
						playerHand[increment] = drawCard();
						if (resolvePlayerBust(playerHand, bet)) {
							return -bet;
						}
						
						// Five card rule
						System.out.println();
						System.out.println(ANSI_SILVER + "You:");
						for (int i = 0; i <= 4; i++) {
						if (playerHand[i] != -1) {
						convertCard(playerHand[i]);
							}
						}
						System.out.println();
						System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
						System.out.print(ANSI_GOLD + "Five card rule! (+");
						return bet;
						
					}
					break;
				case 2:
					
					int dealerValue = 0;
					int playerValue = 0;
					
					while (calculateValue(dealerHand) < 17) {
						int increment2 = 0;
						while ((dealerHand[increment2] != -1) && (increment2 < 5)) {
							increment2++;
						}
						dealerHand[increment2] = drawCard();
					}
					System.out.println();
					System.out.println(ANSI_SILVER + "Dealer:");
					for (int i = 0; i <= 4; i++) {
						if (dealerHand[i] != -1) {
							convertCard(dealerHand[i]);
						}
					}
					System.out.println();
					if (checkAce(dealerHand)) {
						if (calculateValue(dealerHand) <= 11) {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(dealerHand) + "/" + (calculateValue(dealerHand) + 10) + "\n");
							dealerValue = calculateValue(dealerHand) + 10;
						} else {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(dealerHand) + "\n");
							dealerValue = calculateValue(dealerHand);
						}
					} else {
						System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(dealerHand) + "\n");
						dealerValue = calculateValue(dealerHand);
					}
					System.out.println(ANSI_SILVER + "You:");
					for (int i = 0; i <= 4; i++) {
						if (playerHand[i] != -1) {
							convertCard(playerHand[i]);
						}
					}
					System.out.println();
					if (checkAce(playerHand)) {
						if (calculateValue(playerHand) <= 11) {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "/" + (calculateValue(playerHand) + 10) + "\n");
							playerValue = calculateValue(playerHand) + 10;
						} else {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
							playerValue = calculateValue(playerHand);
						}
					} else {
						System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
						playerValue = calculateValue(playerHand);
					}
					
					if (dealerValue > 21) {
						System.out.print(ANSI_GOLD + "Dealer busted. You win! (+");
						return bet;
					} else if (playerValue > dealerValue && playerValue == 21) {
						System.out.print(ANSI_GOLD + "Blackjack! (+");
						return (int)(Math.ceil(bet * 1.5));
					} else if (playerValue > dealerValue) {
						System.out.print(ANSI_GOLD + "You win! (+");
						return bet;
					} else if (playerValue == dealerValue) {
						System.out.print(ANSI_RESET + "Draw. (+");
						return 0;
					} else {
						System.out.print(ANSI_RED + "You lose! (");
						return -(bet);
					}
				case 3:

					increment = 0;
					while ((playerHand[increment] != -1) && (increment < 5)) {
						increment++;
					}
					playerHand[increment] = drawCard();
					if (resolvePlayerBust(playerHand, bet)) {
						return -(bet * 2);
					}
					
					dealerValue = 0;
					playerValue = 0;
					
					while (calculateValue(dealerHand) < 17) {
						int increment2 = 0;
						while ((dealerHand[increment2] != -1) && (increment2 < 5)) {
							increment2++;
						}
						dealerHand[increment2] = drawCard();
					}
					System.out.println();
					System.out.println(ANSI_SILVER + "Dealer:");
					for (int i = 0; i <= 4; i++) {
						if (dealerHand[i] != -1) {
							convertCard(dealerHand[i]);
						}
					}
					System.out.println();
					if (checkAce(dealerHand)) {
						if (calculateValue(dealerHand) <= 11) {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(dealerHand) + "/" + (calculateValue(dealerHand) + 10) + "\n");
							dealerValue = calculateValue(dealerHand) + 10;
						} else {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(dealerHand) + "\n");
							dealerValue = calculateValue(dealerHand);
						}
					} else {
						System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(dealerHand) + "\n");
						dealerValue = calculateValue(dealerHand);
					}
					System.out.println(ANSI_SILVER + "You:");
					for (int i = 0; i <= 4; i++) {
						if (playerHand[i] != -1) {
							convertCard(playerHand[i]);
						}
					}
					System.out.println();
					if (checkAce(playerHand)) {
						if (calculateValue(playerHand) <= 11) {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "/" + (calculateValue(playerHand) + 10) + "\n");
							playerValue = calculateValue(playerHand) + 10;
						} else {
							System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
							playerValue = calculateValue(playerHand);
						}
					} else {
						System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
						playerValue = calculateValue(playerHand);
					}
					
					if (dealerValue > 21) {
						System.out.print(ANSI_GOLD + "Dealer busted. You win! (+");
						return bet * 2;
					} else if (playerValue > dealerValue && playerValue == 21) {
						System.out.print(ANSI_GOLD + "Blackjack! (+");
						return bet * 3;
					} else if (playerValue > dealerValue) {
						System.out.print(ANSI_GOLD + "You win! (+");
						return bet * 2;
					} else if (playerValue == dealerValue) {
						System.out.print(ANSI_RESET + "Draw. (+");
						return 0;
					} else {
						System.out.print(ANSI_RED + "You lose! (");
						return -(bet * 2);
					}
				case 4:
					
					break;
			}
		
			System.out.println();
		
		}
	
		input.close();
		return 0;
	
	}
	
	public static boolean resolvePlayerBust(int[] playerHand, int bet) {
		final String ANSI_RESET = "\u001B[0m";
		final String ANSI_SILVER = "\u001B[38;2;191;191;191m";
		final String ANSI_RED = "\u001B[38;2;255;128;128m";
		
		if (calculateValue(playerHand) > 21) {
			System.out.println();
			System.out.println(ANSI_SILVER + "You:");
			for (int i = 0; i <= 4; i++) {
				if (playerHand[i] != -1) {
					convertCard(playerHand[i]);
				}
			}
			System.out.println();
			System.out.println(ANSI_SILVER + "Value: " + ANSI_RESET + calculateValue(playerHand) + "\n");
			System.out.print(ANSI_RED + "You busted! (");
			return true;
		}
		return false;
	}
	
	public static int calculateValue(int[] a) {
		int value = 0;
		for (int i = 0; i <= 4; i++) {
			if (a[i] != -1) {
				int rank = a[i] % 13;
				int cardVal;
				
				if (rank == 1) {
					cardVal = 1;
				} else if (rank == 11 || rank == 12 || rank == 0) {
					cardVal = 10;
				} else {
					cardVal = rank;
				}
				value += cardVal;
			}
		}
		return (value);
	}
	
	public static boolean checkAce(int[] a) {
		boolean acePresent = false;
		for (int i = 0; i <= 4; i++) {
			if (a[i] != -1) {
				int rank = a[i] % 13;
				
				if (rank == 1) {
					acePresent = true;
				}
			}
		}
		return (acePresent);
	}
	
	public static int drawCard() {
		final int NUMBER_OF_CARDS = 52;
		return (int)(Math.random() * NUMBER_OF_CARDS);
	}
	
	public static void convertCard(int cardNum) {

		final String ANSI_BLACK = "\u001B[38;2;128;128;128m";
		final String ANSI_RED = "\u001B[38;2;255;128;128m";
		
		int suit = cardNum / 13;
		String suitStr = "";
		int rank = cardNum % 13;
		String rankStr = "";
		
		if (suit == 0) {
			suitStr += "♦";
		} else if (suit == 1) {
			suitStr += "♥";
		} else if (suit == 2) {
			suitStr += "♣";
		} else {
			suitStr += "♠";
		}
		
		if (rank == 1) {
			rankStr += "A";
		} else if (rank == 11) {
			rankStr += "J";
		} else if (rank == 12) {
			rankStr += "Q";
		} else if (rank == 0) {
			rankStr += "K";
		} else {
			rankStr += rank;
		}
		
		if (suit <= 1) {
			System.out.print(ANSI_RED + "[" + rankStr + suitStr + "]");
		} else {
			System.out.print(ANSI_BLACK + "[" + rankStr + suitStr + "]");
		}
		
	}

}
