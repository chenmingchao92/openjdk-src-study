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

package sun.text.resources.cldr.zh;

import java.util.ListResourceBundle;

public class FormatData_zh_Hant extends ListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final Object[][] data = new Object[][] {
            { "standalone.MonthAbbreviations",
                new String[] {
                    "1\u6708",
                    "2\u6708",
                    "3\u6708",
                    "4\u6708",
                    "5\u6708",
                    "6\u6708",
                    "7\u6708",
                    "8\u6708",
                    "9\u6708",
                    "10\u6708",
                    "11\u6708",
                    "12\u6708",
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
            { "DayAbbreviations",
                new String[] {
                    "\u9031\u65e5",
                    "\u9031\u4e00",
                    "\u9031\u4e8c",
                    "\u9031\u4e09",
                    "\u9031\u56db",
                    "\u9031\u4e94",
                    "\u9031\u516d",
                }
            },
            { "QuarterNames",
                new String[] {
                    "\u7b2c1\u5b63",
                    "\u7b2c2\u5b63",
                    "\u7b2c3\u5b63",
                    "\u7b2c4\u5b63",
                }
            },
            { "Eras",
                new String[] {
                    "\u897f\u5143\u524d",
                    "\u897f\u5143",
                }
            },
            { "field.era", "\u5e74\u4ee3" },
            { "field.year", "\u5e74" },
            { "field.month", "\u6708" },
            { "field.week", "\u9031" },
            { "field.weekday", "\u9031\u5929" },
            { "field.dayperiod", "\u4e0a\u5348/\u4e0b\u5348" },
            { "field.hour", "\u5c0f\u6642" },
            { "field.minute", "\u5206\u9418" },
            { "field.second", "\u79d2" },
            { "field.zone", "\u6642\u5340" },
            { "TimePatterns",
                new String[] {
                    "zzzzah\u6642mm\u5206ss\u79d2",
                    "zah\u6642mm\u5206ss\u79d2",
                    "ah:mm:ss",
                    "ah:mm",
                }
            },
            { "DatePatterns",
                new String[] {
                    "y\u5e74M\u6708d\u65e5EEEE",
                    "y\u5e74M\u6708d\u65e5",
                    "yyyy/M/d",
                    "y/M/d",
                }
            },
            { "DateTimePatterns",
                new String[] {
                    "{1} {0}",
                }
            },
            { "buddhist.Eras",
                new String[] {
                    "BC",
                    "\u4f5b\u66c6",
                }
            },
            { "java.time.buddhist.DatePatterns",
                new String[] {
                    "Gy\u5e74M\u6708d\u65e5EEEE",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gy/M/d",
                    "Gy/M/d",
                }
            },
            { "buddhist.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy/M/d",
                    "GGGGy/M/d",
                }
            },
            { "japanese.Eras",
                new String[] {
                    "\u897f\u5143",
                    "\u660e\u6cbb",
                    "\u5927\u6b63",
                    "\u662d\u548c",
                    "\u5e73\u6210",
                    "",
                }
            },
            { "java.time.japanese.DatePatterns",
                new String[] {
                    "Gy\u5e74M\u6708d\u65e5EEEE",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gy/M/d",
                    "Gy/M/d",
                }
            },
            { "japanese.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy/M/d",
                    "GGGGy/M/d",
                }
            },
            { "roc.Eras",
                new String[] {
                    "\u6c11\u570b\u524d",
                    "\u6c11\u570b",
                }
            },
            { "java.time.roc.DatePatterns",
                new String[] {
                    "Gy\u5e74M\u6708d\u65e5EEEE",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gy/M/d",
                    "Gy/M/d",
                }
            },
            { "roc.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy/M/d",
                    "GGGGy/M/d",
                }
            },
            { "islamic.MonthNames",
                new String[] {
                    "\u7a46\u54c8\u862d\u59c6\u6708",
                    "\u8272\u6cd5\u723e\u6708",
                    "\u8cf4\u6bd4\u6708 I",
                    "\u8cf4\u6bd4\u6708 II",
                    "\u4e3b\u99ac\u9054\u6708 I",
                    "\u4e3b\u99ac\u9054\u6708 II",
                    "\u8cf4\u54f2\u535c\u6708",
                    "\u820d\u723e\u90a6\u6708",
                    "\u8cf4\u8cb7\u4e39\u6708",
                    "\u9583\u74e6\u9b6f\u6708",
                    "\u90fd\u723e\u5580\u723e\u5fb7\u6708",
                    "\u90fd\u723e\u9ed1\u54f2\u6708",
                    "",
                }
            },
            { "java.time.islamic.DatePatterns",
                new String[] {
                    "Gy\u5e74M\u6708d\u65e5EEEE",
                    "Gy\u5e74M\u6708d\u65e5",
                    "Gy/M/d",
                    "Gy/M/d",
                }
            },
            { "islamic.DatePatterns",
                new String[] {
                    "GGGGy\u5e74M\u6708d\u65e5EEEE",
                    "GGGGy\u5e74M\u6708d\u65e5",
                    "GGGGy/M/d",
                    "GGGGy/M/d",
                }
            },
            { "calendarname.islamic-civil", "\u4f0a\u65af\u862d\u57ce\u5e02\u66c6\u6cd5" },
            { "calendarname.islamicc", "\u4f0a\u65af\u862d\u57ce\u5e02\u66c6\u6cd5" },
            { "calendarname.buddhist", "\u4f5b\u6559\u66c6\u6cd5" },
            { "calendarname.islamic", "\u4f0a\u65af\u862d\u66c6\u6cd5" },
            { "calendarname.gregorian", "\u516c\u66c6" },
            { "calendarname.gregory", "\u516c\u66c6" },
            { "calendarname.roc", "\u6c11\u570b\u66c6" },
            { "calendarname.japanese", "\u65e5\u672c\u66c6\u6cd5" },
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
                    "\u975e\u6578\u503c",
                }
            },
            { "NumberPatterns",
                new String[] {
                    "#,##0.###",
                    "\u00a4#,##0.00",
                    "#,##0%",
                }
            },
        };
        return data;
    }
}
