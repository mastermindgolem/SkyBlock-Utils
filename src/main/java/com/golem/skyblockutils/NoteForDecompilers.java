package com.golem.skyblockutils;

public @interface NoteForDecompilers {
	String value() default "lol this is only here because some fucks will say stupid shit while decompiling the mod (there's probably a million other ways to do it but this is most fun)"; //
	String toNote() default "The default string is not written by Kami. Kami does not curse";
}