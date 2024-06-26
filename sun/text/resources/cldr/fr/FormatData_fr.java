/*
 * Copyright (c) 2012, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * COPYRIGHT AND PERMISSION NOTICE
 *
 * Copyright (C) 1991-2012 Unicode, Inc. All rights reserved. Distributed under
 * the Terms of Use in http://www.unicode.org/copyright.html.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of the Unicode data files and any associated documentation (the "Data
 * Files") or Unicode software and any associated documentation (the
 * "Software") to deal in the Data Files or Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, and/or sell copies of the Data Files or Software, and
 * to permit persons to whom the Data Files or Software are furnished to do so,
 * provided that (a) the above copyright notice(s) and this permission notice
 * appear with all copies of the Data Files or Software, (b) both the above
 * copyright notice(s) and this permission notice appear in associated
 * documentation, and (c) there is clear notice in each modified Data File or
 * in the Software as well as in the documentation associated with the Data
 * File(s) or Software that the data or software has been modified.
 *
 * THE DATA FILES AND SOFTWARE ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF
 * THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS
 * INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR
 * CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE
 * OF THE DATA FILES OR SOFTWARE.
 *
 * Except as contained in this notice, the name of a copyright holder shall not
 * be used in advertising or otherwise to promote the sale, use or other
 * dealings in these Data Files or Software without prior written authorization
 * of the copyright holder.
 */

package sun.text.resources.cldr.fr;

import java.util.ListResourceBundle;

public class FormatData_fr extends ListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final Object[][] data = new Object[][] {
            { "MonthNames",
                new String[] {
                    "janvier",
                    "f\u00e9vrier",
                    "mars",
                    "avril",
                    "mai",
                    "juin",
                    "juillet",
                    "ao\u00fbt",
                    "septembre",
                    "octobre",
                    "novembre",
                    "d\u00e9cembre",
                    "",
                }
            },
            { "MonthAbbreviations",
                new String[] {
                    "janv.",
                    "f\u00e9vr.",
                    "mars",
                    "avr.",
                    "mai",
                    "juin",
                    "juil.",
                    "ao\u00fbt",
                    "sept.",
                    "oct.",
                    "nov.",
                    "d\u00e9c.",
                    "",
                }
            },
            { "standalone.MonthAbbreviations",
                new String[] {
                    "janv.",
                    "f\u00e9vr.",
                    "mars",
                    "avr.",
                    "",
                    "",
                    "juil.",
                    "",
                    "sept.",
                    "oct.",
                    "nov.",
                    "d\u00e9c.",
                    "",
                }
            },
            { "MonthNarrows",
                new String[] {
                    "J",
                    "F",
                    "M",
                    "A",
                    "M",
                    "J",
                    "J",
                    "A",
                    "S",
                    "O",
                    "N",
                    "D",
                    "",
                }
            },
            { "DayNames",
                new String[] {
                    "dimanche",
                    "lundi",
                    "mardi",
                    "mercredi",
                    "jeudi",
                    "vendredi",
                    "samedi",
                }
            },
            { "DayAbbreviations",
                new String[] {
                    "dim.",
                    "lun.",
                    "mar.",
                    "mer.",
                    "jeu.",
                    "ven.",
                    "sam.",
                }
            },
            { "standalone.DayAbbreviations",
                new String[] {
                    "dim.",
                    "lun.",
                    "mar.",
                    "mer.",
                    "jeu.",
                    "ven.",
                    "sam.",
                }
            },
            { "DayNarrows",
                new String[] {
                    "D",
                    "L",
                    "M",
                    "M",
                    "J",
                    "V",
                    "S",
                }
            },
            { "QuarterNames",
                new String[] {
                    "1er trimestre",
                    "2e trimestre",
                    "3e trimestre",
                    "4e trimestre",
                }
            },
            { "QuarterAbbreviations",
                new String[] {
                    "T1",
                    "T2",
                    "T3",
                    "T4",
                }
            },
            { "QuarterNarrows",
                new String[] {
                    "T1",
                    "T2",
                    "T3",
                    "T4",
                }
            },
            { "narrow.AmPmMarkers",
                new String[] {
                    "",
                    "p",
                }
            },
            { "long.Eras",
                new String[] {
                    "avant J\u00e9sus-Christ",
                    "apr\u00e8s J\u00e9sus-Christ",
                }
            },
            { "Eras",
                new String[] {
                    "av. J.-C.",
                    "ap. J.-C.",
                }
            },
            { "field.era", "\u00e8re" },
            { "field.year", "ann\u00e9e" },
            { "field.month", "mois" },
            { "field.week", "semaine" },
            { "field.weekday", "jour de la semaine" },
            { "field.dayperiod", "cadran" },
            { "field.hour", "heure" },
            { "field.minute", "minute" },
            { "field.second", "seconde" },
            { "field.zone", "fuseau horaire" },
            { "TimePatterns",
                new String[] {
                    "HH:mm:ss zzzz",
                    "HH:mm:ss z",
                    "HH:mm:ss",
                    "HH:mm",
                }
            },
            { "DatePatterns",
                new String[] {
                    "EEEE d MMMM y",
                    "d MMMM y",
                    "d MMM y",
                    "dd/MM/yy",
                }
            },
            { "buddhist.long.Eras",
                new String[] {
                    "BC",
                    "\u00e8re bouddhiste",
                }
            },
            { "buddhist.Eras",
                new String[] {
                    "BC",
                    "\u00e8re b.",
                }
            },
            { "buddhist.narrow.Eras",
                new String[] {
                    "BC",
                    "E.B.",
                }
            },
            { "java.time.buddhist.DatePatterns",
                new String[] {
                    "EEEE d MMMM y G",
                    "d MMMM y G",
                    "d MMM, y G",
                    "d/M/yyyy",
                }
            },
            { "buddhist.DatePatterns",
                new String[] {
                    "EEEE d MMMM y GGGG",
                    "d MMMM y GGGG",
                    "d MMM, y GGGG",
                    "d/M/yyyy",
                }
            },
            { "java.time.japanese.DatePatterns",
                new String[] {
                    "EEEE d MMMM y G",
                    "d MMMM y G",
                    "d MMM, y G",
                    "d/M/y GGGGG",
                }
            },
            { "japanese.DatePatterns",
                new String[] {
                    "EEEE d MMMM y GGGG",
                    "d MMMM y GGGG",
                    "d MMM, y GGGG",
                    "d/M/y G",
                }
            },
            { "roc.Eras",
                new String[] {
                    "avant RdC",
                    "RdC",
                }
            },
            { "java.time.roc.DatePatterns",
                new String[] {
                    "EEEE d MMMM y G",
                    "d MMMM y G",
                    "d MMM, y G",
                    "d/M/y GGGGG",
                }
            },
            { "roc.DatePatterns",
                new String[] {
                    "EEEE d MMMM y GGGG",
                    "d MMMM y GGGG",
                    "d MMM, y GGGG",
                    "d/M/y G",
                }
            },
            { "islamic.MonthNames",
                new String[] {
                    "Mouharram",
                    "Safar",
                    "Rabi\u02bb-oul-Aououal",
                    "Rabi\u02bb-out-Tani",
                    "Djoumada-l-Oula",
                    "Djoumada-t-Tania",
                    "Radjab",
                    "Cha\u02bbban",
                    "Ramadan",
                    "Chaououal",
                    "Dou-l-Qa\u02bbda",
                    "Dou-l-Hidjja",
                    "",
                }
            },
            { "islamic.MonthAbbreviations",
                new String[] {
                    "Mouh.",
                    "Saf.",
                    "Rabi\u02bb-oul-A.",
                    "Rabi\u02bb-out-T.",
                    "Djoum.-l-O.",
                    "Djoum.-t-T.",
                    "Radj.",
                    "Cha.",
                    "Ram.",
                    "Chaou.",
                    "Dou-l-Q.",
                    "Dou-l-H.",
                    "",
                }
            },
            { "islamic.Eras",
                new String[] {
                    "",
                    "AH",
                }
            },
            { "java.time.islamic.DatePatterns",
                new String[] {
                    "EEEE d MMMM y G",
                    "d MMMM y G",
                    "d MMM, y G",
                    "d/M/y G",
                }
            },
            { "islamic.DatePatterns",
                new String[] {
                    "EEEE d MMMM y GGGG",
                    "d MMMM y GGGG",
                    "d MMM, y GGGG",
                    "d/M/y GGGG",
                }
            },
            { "calendarname.islamic-civil", "Calendrier civil musulman" },
            { "calendarname.islamicc", "Calendrier civil musulman" },
            { "calendarname.buddhist", "Calendrier bouddhiste" },
            { "calendarname.islamic", "Calendrier musulman" },
            { "calendarname.gregorian", "Calendrier gr\u00e9gorien" },
            { "calendarname.gregory", "Calendrier gr\u00e9gorien" },
            { "calendarname.roc", "Calendrier r\u00e9publicain chinois" },
            { "calendarname.japanese", "Calendrier japonais" },
            { "DefaultNumberingSystem", "latn" },
            { "latn.NumberElements",
                new String[] {
                    ",",
                    "\u00a0",
                    ";",
                    "%",
                    "0",
                    "#",
                    "-",
                    "E",
                    "\u2030",
                    "\u221e",
                    "NaN",
                }
            },
            { "NumberPatterns",
                new String[] {
                    "#,##0.###",
                    "#,##0.00\u00a0\u00a4",
                    "#,##0\u00a0%",
                }
            },
        };
        return data;
    }
}
