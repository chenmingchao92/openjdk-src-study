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

package sun.text.resources.cldr.pl;

import java.util.ListResourceBundle;

public class FormatData_pl extends ListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final Object[][] data = new Object[][] {
            { "MonthNames",
                new String[] {
                    "stycznia",
                    "lutego",
                    "marca",
                    "kwietnia",
                    "maja",
                    "czerwca",
                    "lipca",
                    "sierpnia",
                    "wrze\u015bnia",
                    "pa\u017adziernika",
                    "listopada",
                    "grudnia",
                    "",
                }
            },
            { "standalone.MonthNames",
                new String[] {
                    "stycze\u0144",
                    "luty",
                    "marzec",
                    "kwiecie\u0144",
                    "maj",
                    "czerwiec",
                    "lipiec",
                    "sierpie\u0144",
                    "wrzesie\u0144",
                    "pa\u017adziernik",
                    "listopad",
                    "grudzie\u0144",
                    "",
                }
            },
            { "MonthAbbreviations",
                new String[] {
                    "sty",
                    "lut",
                    "mar",
                    "kwi",
                    "maj",
                    "cze",
                    "lip",
                    "sie",
                    "wrz",
                    "pa\u017a",
                    "lis",
                    "gru",
                    "",
                }
            },
            { "standalone.MonthAbbreviations",
                new String[] {
                    "sty",
                    "lut",
                    "mar",
                    "kwi",
                    "maj",
                    "cze",
                    "lip",
                    "sie",
                    "wrz",
                    "pa\u017a",
                    "lis",
                    "gru",
                    "",
                }
            },
            { "MonthNarrows",
                new String[] {
                    "s",
                    "l",
                    "m",
                    "k",
                    "m",
                    "c",
                    "l",
                    "s",
                    "w",
                    "p",
                    "l",
                    "g",
                    "",
                }
            },
            { "standalone.MonthNarrows",
                new String[] {
                    "s",
                    "l",
                    "m",
                    "k",
                    "m",
                    "c",
                    "l",
                    "s",
                    "w",
                    "p",
                    "l",
                    "g",
                    "",
                }
            },
            { "DayNames",
                new String[] {
                    "niedziela",
                    "poniedzia\u0142ek",
                    "wtorek",
                    "\u015broda",
                    "czwartek",
                    "pi\u0105tek",
                    "sobota",
                }
            },
            { "standalone.DayNames",
                new String[] {
                    "niedziela",
                    "poniedzia\u0142ek",
                    "wtorek",
                    "\u015broda",
                    "czwartek",
                    "pi\u0105tek",
                    "sobota",
                }
            },
            { "DayAbbreviations",
                new String[] {
                    "niedz.",
                    "pon.",
                    "wt.",
                    "\u015br.",
                    "czw.",
                    "pt.",
                    "sob.",
                }
            },
            { "standalone.DayAbbreviations",
                new String[] {
                    "niedz.",
                    "pon.",
                    "wt.",
                    "\u015br.",
                    "czw.",
                    "pt.",
                    "sob.",
                }
            },
            { "DayNarrows",
                new String[] {
                    "N",
                    "P",
                    "W",
                    "\u015a",
                    "C",
                    "P",
                    "S",
                }
            },
            { "standalone.DayNarrows",
                new String[] {
                    "N",
                    "P",
                    "W",
                    "\u015a",
                    "C",
                    "P",
                    "S",
                }
            },
            { "QuarterNames",
                new String[] {
                    "I kwarta\u0142",
                    "II kwarta\u0142",
                    "III kwarta\u0142",
                    "IV kwarta\u0142",
                }
            },
            { "standalone.QuarterNames",
                new String[] {
                    "I kwarta\u0142",
                    "II kwarta\u0142",
                    "III kwarta\u0142",
                    "IV kwarta\u0142",
                }
            },
            { "QuarterAbbreviations",
                new String[] {
                    "K1",
                    "K2",
                    "K3",
                    "K4",
                }
            },
            { "standalone.QuarterAbbreviations",
                new String[] {
                    "1 kw.",
                    "2 kw.",
                    "3 kw.",
                    "4 kw.",
                }
            },
            { "QuarterNarrows",
                new String[] {
                    "1",
                    "2",
                    "3",
                    "4",
                }
            },
            { "long.Eras",
                new String[] {
                    "p.n.e.",
                    "n.e.",
                }
            },
            { "Eras",
                new String[] {
                    "p.n.e.",
                    "n.e.",
                }
            },
            { "field.era", "Era" },
            { "field.year", "Rok" },
            { "field.month", "Miesi\u0105c" },
            { "field.week", "Tydzie\u0144" },
            { "field.weekday", "Dzie\u0144 tygodnia" },
            { "field.dayperiod", "Dayperiod" },
            { "field.hour", "Godzina" },
            { "field.minute", "Minuta" },
            { "field.second", "Sekunda" },
            { "field.zone", "Strefa" },
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
                    "EEEE, d MMMM y",
                    "d MMMM y",
                    "d MMM y",
                    "dd.MM.yyyy",
                }
            },
            { "DateTimePatterns",
                new String[] {
                    "{1} {0}",
                }
            },
            { "java.time.buddhist.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y G",
                    "d MMMM, y G",
                    "d MMM y G",
                    "dd.MM.yyyy G",
                }
            },
            { "buddhist.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y GGGG",
                    "d MMMM, y GGGG",
                    "d MMM y GGGG",
                    "dd.MM.yyyy GGGG",
                }
            },
            { "java.time.japanese.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y G",
                    "d MMMM, y G",
                    "d MMM y G",
                    "dd.MM.yyyy G",
                }
            },
            { "japanese.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y GGGG",
                    "d MMMM, y GGGG",
                    "d MMM y GGGG",
                    "dd.MM.yyyy GGGG",
                }
            },
            { "roc.Eras",
                new String[] {
                    "Przed ROC",
                    "ROC",
                }
            },
            { "java.time.roc.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y G",
                    "d MMMM, y G",
                    "d MMM y G",
                    "dd.MM.yyyy G",
                }
            },
            { "roc.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y GGGG",
                    "d MMMM, y GGGG",
                    "d MMM y GGGG",
                    "dd.MM.yyyy GGGG",
                }
            },
            { "islamic.MonthNames",
                new String[] {
                    "Muharram",
                    "Safar",
                    "Rabi I",
                    "Rabi II",
                    "D\u017cumada I",
                    "D\u017cumada II",
                    "Rad\u017cab",
                    "Szaban",
                    "Ramadan",
                    "Szawwal",
                    "Zu al-kada",
                    "Zu al-hid\u017cd\u017ca",
                    "",
                }
            },
            { "islamic.MonthAbbreviations",
                new String[] {
                    "Muh.",
                    "Saf.",
                    "Rab. I",
                    "Rab. II",
                    "D\u017cu. I",
                    "D\u017cu. II",
                    "Ra.",
                    "Sza.",
                    "Ram.",
                    "Szaw.",
                    "Zu al-k.",
                    "Zu al-h.",
                    "",
                }
            },
            { "islamic.MonthNarrows",
                new String[] {
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "",
                }
            },
            { "java.time.islamic.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y G",
                    "d MMMM, y G",
                    "d MMM y G",
                    "dd.MM.yyyy G",
                }
            },
            { "islamic.DatePatterns",
                new String[] {
                    "EEEE, d MMMM, y GGGG",
                    "d MMMM, y GGGG",
                    "d MMM y GGGG",
                    "dd.MM.yyyy GGGG",
                }
            },
            { "calendarname.islamic-civil", "kalendarz islamski (metoda obliczeniowa)" },
            { "calendarname.islamicc", "kalendarz islamski (metoda obliczeniowa)" },
            { "calendarname.buddhist", "kalendarz buddyjski" },
            { "calendarname.islamic", "kalendarz islamski (metoda wzrokowa)" },
            { "calendarname.gregorian", "kalendarz gregoria\u0144ski" },
            { "calendarname.gregory", "kalendarz gregoria\u0144ski" },
            { "calendarname.roc", "kalendarz Republiki Chi\u0144skiej" },
            { "calendarname.japanese", "kalendarz japo\u0144ski" },
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
                    "#,##0%",
                }
            },
        };
        return data;
    }
}
