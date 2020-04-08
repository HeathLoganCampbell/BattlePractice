package com.battlechunk.practice.commons;

import org.bukkit.ChatColor;

/**
 * Chat colours as strings
 * 
 * @author Sprock
 */
public class CC
{
	
	public static String
	blue = ChatColor.BLUE.toString(),
	aqua = ChatColor.AQUA.toString(),
	yellow = ChatColor.YELLOW.toString(),
	red = ChatColor.RED.toString(),
	gray = ChatColor.GRAY.toString(),
	gold = ChatColor.GOLD.toString(),
	green = ChatColor.GREEN.toString(),
	white = ChatColor.WHITE.toString(),
	black = ChatColor.BLACK.toString(),
	pink = ChatColor.LIGHT_PURPLE.toString(),

	darkBlue = ChatColor.DARK_BLUE.toString(),
	darkAqua = ChatColor.DARK_AQUA.toString(),
	darkGray = ChatColor.DARK_GRAY.toString(),
	darkGreen = ChatColor.DARK_GREEN.toString(),
	darkPurple = ChatColor.DARK_PURPLE.toString(),
	darkRed = ChatColor.DARK_RED.toString(),
	
	dBlue = darkBlue,
	dAqua = darkAqua,
	dGray = darkGray,
	dGreen = darkGreen,
	dPurple = darkPurple,
	dRed = darkRed,
	
	lightPurple = ChatColor.LIGHT_PURPLE.toString(),
	
	lPurple = lightPurple,
	
	bold = ChatColor.BOLD.toString(),
	magic = ChatColor.MAGIC.toString(),
	italic = ChatColor.ITALIC.toString(),
	strikeThrough = ChatColor.STRIKETHROUGH.toString(),
	reset = ChatColor.RESET.toString(),
	
	b = bold,
	m = magic,
	i = italic,
	s = strikeThrough,
	r = reset,
	
	bBlue = blue + b, 
	bAqua =  aqua  + b,
	bYellow = yellow + b,
	bRed =  red + b,
	bGray = gray + b,
	bGold = gold + b,
	bGreen =  green + b,
	bWhite =  white + b,
	bBlack =  black + b,
	
	bdBlue =  dBlue + b,
	bdAqua = dAqua + b,
	bdGray =  dGray  + b,
	bdGreen =  dGreen + b,
	bdPurple =  dPurple + b,
	bdRed =  dRed + b,
	
	blPurple =  lPurple  + b,
	
	iBlue = blue + i, 
	iAqua =  aqua  + i,
	iYellow = yellow + i,
	iRed =  red + i,
	iGray = gray + i,
	iGold = gold + i,
	iGreen =  green + i,
	iWhite =  white + i,
	iBlack =  black + i,
	
	idBlue =  dBlue + i,
	idAqua = dAqua + i,
	idGray =  dGray  + i,
	idGreen =  dGreen + i,
	idPurple =  dPurple + i,
	idRed =  dRed + i,
	
	ilPurple =  lPurple  + i;

	//unicode
	public static String
	
	thinX = "╳",
	thickX = "✖",
	thickCheck = "✔",
	
	cresentMoon = "☽",
	skull = "☠",
	waves = "♒",
	pencil = "✐",
	cloud = "☁",
	miniStar = "☼",
	largeEmptyStar = "✰",
	largeFullStar = "★",
	checkInBox = "☑",
	xInBox = "☒",
	arrow = "→",
	checkerArrow = "⇛",
	thickArrow="⇨",
	lineVertical = "│",
	doubleLineVertical = "║",
	arrowUp = "⇑",
	arrowUpSolid = "⬆";

	public static String getLastColors(String input)
	{
		String result = "";
		int length = input.length();

		// Search backwards from the end as it is faster
		for (int index = length - 1; index > -1; index--) {
			char section = input.charAt(index);
			if (section == ChatColor.COLOR_CHAR && index < length - 1) {
				char c = input.charAt(index + 1);
				ChatColor color = ChatColor.getByChar(c);

				if (color != null) {
					result = color.toString() + result;

					// Once we find a color or reset we can stop searching
					if (color.isColor() || color.equals(ChatColor.RESET)) {
						break;
					}
				}
			}
		}

		return result;
	}

	public static String error(String header, String message)
	{
		return CC.bAqua + header + CC.red + " ! " + CC.gray + message;
	}

	public static String info(String header, String message)
	{
		return CC.bAqua + header + CC.aqua + " * " + CC.gray + message;
	}

	public static String success(String header, String message)
	{
		return CC.bAqua + header + CC.green + " ✔ " + CC.gray + message;
	}

	public static String announcement(String header, String message)
	{
		return CC.bAqua + header + CC.green + " # " + CC.gray + message;
	}

	public static String gold(long gold)
	{
		return gold(gold + "");
	}

	public static String gold(String gold)
	{
		return CC.gold + gold + " g" + CC.gray;
	}

	public static String command(String command)
	{
		return CC.green + command + CC.gray;
	}

	public  static String highlight(String highlight)
	{
		return  CC.white + highlight + CC.gray;
	}
}