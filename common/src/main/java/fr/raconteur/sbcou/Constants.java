package fr.raconteur.sbcou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

	public static final String MOD_ID = "sbcou";
	public static final String MOD_NAME = "Sbcou";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
	public static final String MINECRAFT_DIR_SBCOU_DB_RELATIVE_PATH = "config/sbcou.db";
	public static final List<String> SBCOU_DATA_TYPES = List.of(
			"INTEGER",
			"REAL",
			"NAN",
			"INFINITY",
			"NULL",
			"BOOLEAN",
			"STRING",
			"LIST",
			"OBJECT"
	);
}