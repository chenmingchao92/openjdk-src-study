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

package sun.text.resources.cldr.he;

import java.util.ListResourceBundle;

public class FormatData_he extends ListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final Object[][] data = new Object[][] {
            { "MonthNames",
                new String[] {
                    "\u05d9\u05e0\u05d5\u05d0\u05e8",
                    "\u05e4\u05d1\u05e8\u05d5\u05d0\u05e8",
                    "\u05de\u05e8\u05e5",
                    "\u05d0\u05e4\u05e8\u05d9\u05dc",
                    "\u05de\u05d0\u05d9",
                    "\u05d9\u05d5\u05e0\u05d9",
                    "\u05d9\u05d5\u05dc\u05d9",
                    "\u05d0\u05d5\u05d2\u05d5\u05e1\u05d8",
                    "\u05e1\u05e4\u05d8\u05de\u05d1\u05e8",
                    "\u05d0\u05d5\u05e7\u05d8\u05d5\u05d1\u05e8",
                    "\u05e0\u05d5\u05d1\u05de\u05d1\u05e8",
                    "\u05d3\u05e6\u05de\u05d1\u05e8",
                    "",
                }
            },
            { "MonthAbbreviations",
                new String[] {
                    "\u05d9\u05e0\u05d5",
                    "\u05e4\u05d1\u05e8",
                    "\u05de\u05e8\u05e5",
                    "\u05d0\u05e4\u05e8",
                    "\u05de\u05d0\u05d9",
                    "\u05d9\u05d5\u05e0",
                    "\u05d9\u05d5\u05dc",
                    "\u05d0\u05d5\u05d2",
                    "\u05e1\u05e4\u05d8",
                    "\u05d0\u05d5\u05e7",
                    "\u05e0\u05d5\u05d1",
                    "\u05d3\u05e6\u05de",
                    "",
                }
            },
            { "standalone.MonthAbbreviations",
                new String[] {
                    "\u05d9\u05e0\u05d5\u05f3",
                    "\u05e4\u05d1\u05e8\u05f3",
                    "\u05de\u05e8\u05e5",
                    "\u05d0\u05e4\u05e8\u05f3",
                    "\u05de\u05d0\u05d9",
                    "\u05d9\u05d5\u05e0\u05f3",
                    "\u05d9\u05d5\u05dc\u05f3",
                    "\u05d0\u05d5\u05d2\u05f3",
                    "\u05e1\u05e4\u05d8\u05f3",
                    "\u05d0\u05d5\u05e7\u05f3",
                    "\u05e0\u05d5\u05d1\u05f3",
                    "\u05d3\u05e6\u05de\u05f3",
                    "",
                }
            },
            { "MonthNarrows",
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
            { "DayNames",
                new String[] {
                    "\u05d9\u05d5\u05dd \u05e8\u05d0\u05e9\u05d5\u05df",
                    "\u05d9\u05d5\u05dd \u05e9\u05e0\u05d9",
                    "\u05d9\u05d5\u05dd \u05e9\u05dc\u05d9\u05e9\u05d9",
                    "\u05d9\u05d5\u05dd \u05e8\u05d1\u05d9\u05e2\u05d9",
                    "\u05d9\u05d5\u05dd \u05d7\u05de\u05d9\u05e9\u05d9",
                    "\u05d9\u05d5\u05dd \u05e9\u05d9\u05e9\u05d9",
                    "\u05d9\u05d5\u05dd \u05e9\u05d1\u05ea",
                }
            },
            { "DayAbbreviations",
                new String[] {
                    "\u05d9\u05d5\u05dd \u05d0\u05f3",
                    "\u05d9\u05d5\u05dd \u05d1\u05f3",
                    "\u05d9\u05d5\u05dd \u05d2\u05f3",
                    "\u05d9\u05d5\u05dd \u05d3\u05f3",
                    "\u05d9\u05d5\u05dd \u05d4\u05f3",
                    "\u05d9\u05d5\u05dd \u05d5\u05f3",
                    "\u05e9\u05d1\u05ea",
                }
            },
            { "DayNarrows",
                new String[] {
                    "\u05d0",
                    "\u05d1",
                    "\u05d2",
                    "\u05d3",
                    "\u05d4",
                    "\u05d5",
                    "\u05e9",
                }
            },
            { "standalone.DayNarrows",
                new String[] {
                    "\u05d0",
                    "\u05d1",
                    "\u05d2",
                    "\u05d3",
                    "\u05d4",
                    "\u05d5",
                    "\u05e9",
                }
            },
            { "QuarterNames",
                new String[] {
                    "\u05e8\u05d1\u05e2\u05d5\u05df 1",
                    "\u05e8\u05d1\u05e2\u05d5\u05df 2",
                    "\u05e8\u05d1\u05e2\u05d5\u05df 3",
                    "\u05e8\u05d1\u05e2\u05d5\u05df 4",
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
            { "AmPmMarkers",
                new String[] {
                    "\u05dc\u05e4\u05e0\u05d4\u05f4\u05e6",
                    "\u05d0\u05d7\u05d4\u05f4\u05e6",
                }
            },
            { "long.Eras",
                new String[] {
                    "\u05dc\u05e4\u05e0\u05d9 \u05d4\u05e1\u05e4\u05d9\u05e8\u05d4",
                    "\u05dc\u05e1\u05e4\u05d9\u05e8\u05d4",
                }
            },
            { "Eras",
                new String[] {
                    "\u05dc\u05e4\u05e0\u05d4\u05f4\u05e1",
                    "\u05dc\u05e1\u05d4\u05f4\u05e0",
                }
            },
            { "field.era", "\u05ea\u05e7\u05d5\u05e4\u05d4" },
            { "field.year", "\u05e9\u05e0\u05d4" },
            { "field.month", "\u05d7\u05d5\u05d3\u05e9" },
            { "field.week", "\u05e9\u05d1\u05d5\u05e2" },
            { "field.weekday", "\u05d9\u05d5\u05dd \u05d1\u05e9\u05d1\u05d5\u05e2" },
            { "field.dayperiod", "\u05dc\u05e4\u05d4\u05f4\u05e6/\u05d0\u05d7\u05d4\u05f4\u05e6" },
            { "field.hour", "\u05e9\u05e2\u05d4" },
            { "field.minute", "\u05d3\u05e7\u05d4" },
            { "field.second", "\u05e9\u05e0\u05d9\u05d9\u05d4" },
            { "field.zone", "\u05d0\u05d6\u05d5\u05e8" },
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
                    "EEEE, d \u05d1MMMM y",
                    "d \u05d1MMMM y",
                    "d \u05d1MMM yyyy",
                    "dd/MM/yy",
                }
            },
            { "islamic.MonthNames",
                new String[] {
                    "\u05de\u05d5\u05d7\u05e8\u05dd",
                    "\u05e1\u05e4\u05e8",
                    "\u05e8\u05d1\u05d9\u05e2 \u05d0\u05dc-\u05d0\u05d5\u05d5\u05d0\u05dc",
                    "\u05e8\u05d1\u05d9\u05e2 \u05d0\u05dc-\u05ea\u05e0\u05d9",
                    "\u05d2\u05f3\u05d5\u05de\u05d3\u05d4 \u05d0\u05dc-\u05d0\u05d5\u05d5\u05d0\u05dc",
                    "\u05d2\u05f3\u05d5\u05de\u05d3\u05d4 \u05d0\u05dc-\u05ea\u05e0\u05d9",
                    "\u05e8\u05d2\u05f3\u05d0\u05d1",
                    "\u05e9\u05e2\u05d1\u05d0\u05df",
                    "\u05e8\u05d0\u05de\u05d3\u05df",
                    "\u05e9\u05d5\u05d5\u05d0\u05dc",
                    "\u05d6\u05d5 \u05d0\u05dc-QI'DAH",
                    "\u05d6\u05d5 \u05d0\u05dc-\u05d7\u05d9\u05d2\u05f3\u05d4",
                    "",
                }
            },
            { "islamic.Eras",
                new String[] {
                    "",
                    "\u05e9\u05e0\u05ea \u05d4\u05d9\u05d2\u05f3\u05e8\u05d4",
                }
            },
            { "calendarname.gregorian", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05d2\u05e8\u05d2\u05d5\u05e8\u05d9\u05d0\u05e0\u05d9" },
            { "calendarname.gregory", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05d2\u05e8\u05d2\u05d5\u05e8\u05d9\u05d0\u05e0\u05d9" },
            { "calendarname.roc", "\u05dc\u05d5\u05d7 \u05d4\u05e9\u05e0\u05d4 \u05d4\u05e1\u05d9\u05e0\u05d9 Minguo" },
            { "calendarname.islamic-civil", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05de\u05d5\u05e1\u05dc\u05de\u05d9-\u05d0\u05d6\u05e8\u05d7\u05d9" },
            { "calendarname.islamicc", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05de\u05d5\u05e1\u05dc\u05de\u05d9-\u05d0\u05d6\u05e8\u05d7\u05d9" },
            { "calendarname.japanese", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05d9\u05e4\u05e0\u05d9" },
            { "calendarname.buddhist", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05d1\u05d5\u05d3\u05d4\u05d9\u05e1\u05d8\u05d9" },
            { "calendarname.islamic", "\u05dc\u05d5\u05d7 \u05e9\u05e0\u05d4 \u05de\u05d5\u05e1\u05dc\u05de\u05d9" },
            { "DefaultNumberingSystem", "latn" },
            { "latn.NumberElements",
                new String[] {
                    ".",
                    ",",
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
