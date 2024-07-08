package com.aproteam.flowerpost;

import java.util.Scanner;

public class Constants {

	public static final Scanner scanner = new Scanner(System.in);
	/**
	 * A variable that validates e-mail using regular expression
	 */
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	public static final String FLOWER_POST_LOGO = """
			 ________   .---.       ,-----.    .--.      .--.    .-''-.  .-------.    .-------.     ,-----.       .-'''-. ,---------. \s
			|        |  | ,_|     .'  .-,  '.  |  |_     |  |  .'_ _   \\ |  _ _   \\   \\  _(`)_ \\  .'  .-,  '.    / _     \\\\          \\\s
			|   .----',-./  )    / ,-.|  \\ _ \\ | _( )_   |  | / ( ` )   '| ( ' )  |   | (_ o._)| / ,-.|  \\ _ \\  (`' )/`--' `--.  ,---'\s
			|  _|____ \\  '_ '`) ;  \\  '_ /  | :|(_ o _)  |  |. (_ o _)  ||(_ o _) /   |  (_,_) /;  \\  '_ /  | :(_ o _).       |   \\   \s
			|_( )_   | > (_)  ) |  _`,/ \\ _/  || (_,_) \\ |  ||  (_,_)___|| (_,_).' __ |   '-.-' |  _`,/ \\ _/  | (_,_). '.     :_ _:   \s
			(_ o._)__|(  .  .-' : (  '\\_/ \\   ;|  |/    \\|  |'  \\   .---.|  |\\ \\  |  ||   |     : (  '\\_/ \\   ;.---.  \\  :    (_I_)   \s
			|(_,_)     `-'`-'|___\\ `"/  \\  ) / |  '  /\\  `  | \\  `-'    /|  | \\ `'   /|   |      \\ `"/  \\  ) / \\    `-'  |   (_(=)_)  \s
			|   |       |        \\'. \\_/``".'  |    /  \\    |  \\       / |  |  \\    / /   )       '. \\_/``".'   \\       /     (_I_)   \s
			'---'       `--------`  '-----'    `---'    `---`   `'-..-'  ''-'   `'-'  `---'         '-----'      `-...-'      '---'   \s
			""";

	public static final int FLOWER_SHOP_MINIMUM_QUANTITY = 30;
	public static final int SIMULATE_ORDER_STATUS_CHANGE_DELAY_IN_SECONDS = 30;

}