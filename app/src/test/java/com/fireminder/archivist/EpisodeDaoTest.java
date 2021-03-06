package com.fireminder.archivist;

import com.fireminder.archivist.model.PodcastTable;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.ParseException;
import java.util.UUID;

@Config(manifest = "src/main/AndroidManifest.xml", sdk = 21, resourceDir = "app/src/test/res/")
@RunWith(RobolectricTestRunner.class)
public class EpisodeDaoTest extends TestCase {

  public static final String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<?xml-stylesheet type=\"text/xsl\" media=\"screen\" href=\"/~d/styles/rss2enclosuresfull.xsl\"?><?xml-stylesheet type=\"text/css\" media=\"screen\" href=\"http://feeds.feedburner.com/~d/styles/itemcontent.css\"?><rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" version=\"2.0\">\n" +
      "<channel>\n" +
      "\n" +
      "\n" +
      "\n" +
      "      <title>Dan Carlin's Hardcore History</title>\n" +
      "       <description>Was Alexander the Great as bad a person as Hitler? What was the greatest army of all time? Which U.S. President was the worst? Hardcore History discusses the issues and questions history fans love.</description>\n" +
      "       <itunes:summary>In \"Hardcore History\" the very unconventional Dan Carlin takes his \"Martian\", outside-the-box way of thinking and applies it to the past. Was Alexander the Great as bad a person as Adolf Hitler? What would Apaches with modern weapons be like? Will our modern civilization ever fall like civilizations from past eras? This is a difficult-to-classify show that has a rather sharp edge. It's not for everyone. But the innovative style and approach has made \"Dan Carlin's Hardcore History\" a New Media hit. </itunes:summary>\n" +
      "       <itunes:subtitle>Was Alexander the Great as bad a person as Hitler? What was the greatest army of all time? Which U.S. President was the worst? Hardcore History discusses the issues and questions history fans love.</itunes:subtitle>\n" +
      "       <link>http://www.dancarlin.com</link>\n" +
      "\n" +
      "                                   <pubDate>Mon, 29 Dec 2014 22:24:12 PST</pubDate>\n" +
      "\n" +
      "\n" +
      "\n" +
      "                  <language>en-us</language>\n" +
      "\n" +
      "                                   <managingEditor>dan@dancarlin.com (Dan Carlin)</managingEditor>\n" +
      "                                   <webMaster>dan@dancarlin.com (Dan Carlin)</webMaster>\n" +
      "\n" +
      "                            <itunes:author>Dan Carlin</itunes:author>\n" +
      "                                   <copyright>dancarlin.com</copyright>\n" +
      "                                   <itunes:image href=\"http://www.dancarlin.com/graphics/DC_HH_iTunes.jpg\" />\n" +
      "                                   <image><url>http://www.dancarlin.com/graphics/DC_HH_iTunes.jpg</url>\n" +
      "                                   <link>http://www.dancarlin.com</link><title>Dan Carlin's Hardcore History</title></image>\n" +
      "                                   <itunes:owner>\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "                              <itunes:name>Dan Carlin's Hardcore History</itunes:name>\n" +
      "                              <itunes:email>dan@dancarlin.com </itunes:email>\n" +
      "                              </itunes:owner>\n" +
      "                              <itunes:keywords>History, Military, War, Ancient, Archaeology, Classics, Carlin</itunes:keywords>\n" +
      "                              <itunes:category text=\"Society &amp; Culture\">\n" +
      "                              <itunes:category text=\"History\" />\n" +
      "                              </itunes:category>\n" +
      "                              <itunes:explicit>no</itunes:explicit>\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "<atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"self\" type=\"application/rss+xml\" href=\"http://feeds.feedburner.com/dancarlin/history\" /><feedburner:info xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\" uri=\"dancarlin/history\" /><atom10:link xmlns:atom10=\"http://www.w3.org/2005/Atom\" rel=\"hub\" href=\"http://pubsubhubbub.appspot.com/\" /><item>\n" +
      "<title>Show 54 - Blueprint for Armageddon V</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha54_Blueprint_for_Armageddon_V.mp3</guid>\n" +
      "<description>Politics, diplomacy, revolution and mutiny take center stage at the start of this episode, but mud, blood, shells and tragedy drown all by the end.</description>\n" +
      "<itunes:subtitle>Politics, diplomacy, revolution and mutiny take center stage at the start of this episode, but mud, blood, shells and tragedy drown all by the end.</itunes:subtitle>\n" +
      "<itunes:summary>Politics, diplomacy, revolution and mutiny take center stage at the start of this episode, but mud, blood, shells and tragedy drown all by the end.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-54-blueprint-for-armageddon-v/</link>\n" +
      "<pubDate>Mon, 29 Dec 2014 22:24:12 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha54_Blueprint_for_Armageddon_V.mp3\" length=\"259429328\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>04:29:57</itunes:duration>\n" +
      "<itunes:keywords>first world war, Great War, Woodrow Wilson, America, United States, Russia, Pancho Villa, Nicholas II, Ludendorff, Hindenburg, Germany, Passchendaele, Ypres, Haig, Lloyd George, Winston Churchill, Neville, Mutinies of 1917, Russian Revolution, submarines</itunes:keywords>\n" +
      "</item>\t\t\t\t\t\t\t\t  \n" +
      "\n" +
      "\n" +
      "\n" +
      "<item>\n" +
      "<title>Show 53 - Blueprint for Armageddon IV</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha53_Blueprint_for_Armageddon_IV.mp3</guid>\n" +
      "<description>Machine guns, barbed wire and millions upon millions of artillery shells create industrialized meat grinders at Verdun and the Somme. There's never been a human experience like it and it changes a generation.</description>\n" +
      "<itunes:subtitle>Machine guns, barbed wire and millions upon millions of artillery shells create industrialized meat grinders at Verdun and the Somme. There's never been a human experience like it and it changes a generation.</itunes:subtitle>\n" +
      "<itunes:summary>Machine guns, barbed wire and millions upon millions of artillery shells create industrialized meat grinders at Verdun and the Somme. There's never been a human experience like it and it changes a generation.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-53-blueprint-for-armageddon-iv</link>\n" +
      "<pubDate>Sun, 17 Aug 2014 09:21:10 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha53_Blueprint_for_Armageddon_IV.mp3\" length=\"226905886\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>03:55:51</itunes:duration>\n" +
      "<itunes:keywords>World War One, Great War, history, Europe, history, podcast, war, military, 1916, Britain, France, Russia, Austria-Hungary, tank, artillary, battles, army, Germany, trenches, poison gas, Churchill, Verdun, Somme, Jutland, Naval</itunes:keywords>\n" +
      "\n" +
      "</item>\t\t\t\t\t\t\t\t  \n" +
      "\n" +
      "\n" +
      "<item>\n" +
      "<title>Show 52 - Blueprint for Armageddon III</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha52_Blueprint_for_Armageddon_III.mp3</guid>\n" +
      "<description>The war of maneuver that was supposed to be over quickly instead turns into a lingering bloody stalemate. Trench warfare begins, and with it, all the murderous efforts on both sides to overcome the static defenses.</description>\n" +
      "<itunes:subtitle>The war of maneuver that was supposed to be over quickly instead turns into a lingering bloody stalemate. Trench warfare begins, and with it, all the murderous efforts on both sides to overcome the static defenses.</itunes:subtitle>\n" +
      "<itunes:summary>The war of maneuver that was supposed to be over quickly instead turns into a lingering bloody stalemate. Trench warfare begins, and with it, all the murderous efforts on both sides to overcome the static defenses.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-52-blueprint-for-armageddon-iii</link>\n" +
      "<pubDate>Thu, 24 Apr 2014 12:25:18 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha52_Blueprint_for_Armageddon_III.mp3\" length=\"225609410\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>03:54:08</itunes:duration>\n" +
      "<itunes:keywords>World War One, Great War, history, Italy, Europe, history, podcast, war, military, 1915, Britain, France, Russia, Austria-Hungary, Serbia, Turkey, battles, army, Armenians, Germany, trenches, poison gas, Churchill, Ypres, Gallipoli, Artillery </itunes:keywords>\n" +
      "\n" +
      "</item>\t\n" +
      "\n" +
      "\n" +
      "<item>\n" +
      "<title>Show 51 - Blueprint for Armageddon II</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha51_Blueprint_for_Armageddon_II.mp3</guid>\n" +
      "<description>The Great Powers all come out swinging in the first round of the worst war the planet has ever seen. Millions of men in dozens of armies vie in the most deadly and complex opening moves of any conflict in world history.</description>\n" +
      "<itunes:subtitle>The Great Powers all come out swinging in the first round of the worst war the planet has ever seen. Millions of men in dozens of armies vie in the most deadly and complex opening moves of any conflict in world history.</itunes:subtitle>\n" +
      "<itunes:summary>The Great Powers all come out swinging in the first round of the worst war the planet has ever seen. Millions of men in dozens of armies vie in the most deadly and complex opening moves of any conflict in world history.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-51-blueprint-for-armageddon-ii</link>\n" +
      "<pubDate>Thu, 30 Jan 2014 21:12:15 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha51_Blueprint_for_Armageddon_II.mp3\" length=\"193299910\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>03:20:29</itunes:duration>\n" +
      "<itunes:keywords>First World War, World War One, Great War, Kaiser, Czar, Europe, history, podcast, war, military, 1914, Britain, France, Russia, Austria-Hungary, Serbia, Napoleon, battles, army, Belgium, Ludendorff, Moltke, Joffre, Mons, Marne, Tannenberg, Artillery</itunes:keywords>\n" +
      "\n" +
      "</item>\t\t\t\t\t\t\t\t  \n" +
      "\t\n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "<item>\n" +
      "<title>Show 50 - Blueprint for Armageddon I</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha50_Blueprint_for_Armageddon_I.mp3</guid>\n" +
      "<description>The planet hadn't seen a major war between all the Great Powers since the downfall of Napoleon at Waterloo in 1815. But 99 years later the dam breaks and a Pandora's Box of violence engulfs the planet.</description>\n" +
      "<itunes:subtitle>The planet hadn't seen a major war between all the Great Powers since the downfall of Napoleon at Waterloo in 1815. But 99 years later the dam breaks and a Pandora's Box of violence engulfs the planet.</itunes:subtitle>\n" +
      "<itunes:summary>The planet hadn't seen a major war between all the Great Powers since the downfall of Napoleon at Waterloo in 1815. But 99 years later the dam breaks and a Pandora's Box of violence engulfs the planet.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-50-blueprint-for-armageddon-i</link>\n" +
      "<pubDate>Tue, 29 Oct 2013 23:12:15 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha50_Blueprint_for_Armageddon_I.mp3\" length=\"180682140\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>03:07:20</itunes:duration>\n" +
      "<itunes:keywords>First World War, World War One, Great War, Kaiser, Czar, Europe, history, podcast, audio, war, military, 1914, Britain, France, Russia, Austria-Hungary, Serbia, Princip, assassination, Bismarck, Napoleon, battles, army, Belgium, Ludendorff</itunes:keywords>\n" +
      "\n" +
      "</item>\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "<item>\n" +
      "<title>Show 49 - The American Peril</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha49_The_American_Peril.mp3</guid>\n" +
      "<description>Imperial temptations and humanitarian nightmares force the United States of the late 19th Century to confront the contradictions between its revolutionary self-image and its expanding national interests.</description>\n" +
      "<itunes:subtitle>Imperial temptations and humanitarian nightmares force the United States of the late 19th Century to confront the contradictions between its revolutionary self-image and its expanding national interests.</itunes:subtitle>\n" +
      "<itunes:summary>Imperial temptations and humanitarian nightmares force the United States of the late 19th Century to confront the contradictions between its revolutionary self-image and its expanding national interests.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-49-the-american-peril</link>\n" +
      "<pubDate>Thu, 25 Jul 2013 14:05:51 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha49_The_American_Peril.mp3\" length=\"236687418\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>04:05:41</itunes:duration>\n" +
      "<itunes:keywords> Spanish-American, Philippine, Insurrection, William McKinley, Theodore Roosevelt, Cuba, Imperialism, expansion, revolution, history, podcast, United States, America, colonialism, Jingoism, Nationalism, patriotism, isolationism, Moro Wars</itunes:keywords>\n" +
      "\n" +
      "</item>\n" +
      "\n" +
      "\n" +
      "\n" +
      "<item>\n" +
      "<title>Show 48 - Prophets of Doom</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha48_Prophets_of_Doom.mp3</guid>\n" +
      "<description>Murderous millennial preachers and prophets take over the German city of Munster after Martin Luther unleashes a Pandora's Box of religious anarchy with the Protestant Reformation.</description>\n" +
      "<itunes:subtitle>Murderous millennial preachers and prophets take over the German city of Munster after Martin Luther unleashes a Pandora's Box of religious anarchy with the Protestant Reformation.</itunes:subtitle>\n" +
      "<itunes:summary>Murderous millennial preachers and prophets take over the German city of Munster after Martin Luther unleashes a Pandora's Box of religious anarchy with the Protestant Reformation.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-48-prophets-of-doom</link>\n" +
      "<pubDate>Mon, 22 Apr 2013 02:27:04 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha48_Prophets_of_Doom.mp3\" length=\"258129561\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>04:28:01</itunes:duration>\n" +
      "<itunes:keywords>Luther, Reformation, history, podcast, Munster, Anabaptist, religious, fanaticism, terrorism, Christian, Catholic, church, Protestant, German, Pope, Renaissance, Leiden, Lutheran, Rome, David Koresh, cults</itunes:keywords>\n" +
      "\n" +
      "</item>\t\n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "<item>\n" +
      "<title>Show 47 - Wrath of the Khans V</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha47_Wrath_of_the_Khans_V.mp3</guid>\n" +
      "<description>Succession issues weaken the Mongol Empire as the grandchildren of Genghis Khan fight over their imperial inheritance. This doesn't stop them from dealing out pain, suffering, and ironically good governance while doing so.</description>\n" +
      "<itunes:subtitle>Succession issues weaken the Mongol Empire as the grandchildren of Genghis Khan fight over their imperial inheritance. This doesn't stop them from dealing out pain, suffering, and ironically good governance while doing so.</itunes:subtitle>\n" +
      "<itunes:summary>Succession issues weaken the Mongol Empire as the grandchildren of Genghis Khan fight over their imperial inheritance. This doesn't stop them from dealing out pain, suffering, and ironically good governance while doing so.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-47-wrath-of-the-khans-v</link>\n" +
      "<pubDate>Sun, 13 Jan 2013 21:42:03 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha47_Wrath_of_the_Khans_V.mp3\" length=\"122630127\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>02:06:52</itunes:duration>\n" +
      "<itunes:keywords>Russia, Persia, Turks, Huns, Asia, podcast, history, Middle Ages, invasion, conquest, Ogedai, Batu, Tartars, Muslim, Crusades, Baghdad, Mameluke, military , Mongke, Guyuk, Kublai, Golden Horde, Helegu, Egypt Mongols, Genghis, Chingis, Jengiz, Khan</itunes:keywords>\n" +
      "\n" +
      "</item>\t\n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "<item>\n" +
      "<title>Show 46 - Wrath of the Khans IV</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha46_Wrath_of_the_Khans_IV.mp3</guid>\n" +
      "<description>The death of Genghis Khan, the founder of the Mongol Empire, should have slowed the momentum of Mongol conquests, but instead it accelerated it. This time though, all of Europe is on the Mongol hit list. </description>\n" +
      "<itunes:subtitle>The death of Genghis Khan, the founder of the Mongol Empire, should have slowed the momentum of Mongol conquests, but instead it accelerated it. This time though, all of Europe is on the Mongol hit list. </itunes:subtitle>\n" +
      "<itunes:summary>The death of Genghis Khan, the founder of the Mongol Empire, should have slowed the momentum of Mongol conquests, but instead it accelerated it. This time though, all of Europe is on the Mongol hit list. </itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-46-wrath-of-the-khans-iv</link>\n" +
      "<pubDate>Tue, 13 Nov 2012 00:33:12 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha46_Wrath_of_the_Khans_IV.mp3\" length=\"88502087\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>01:31:19</itunes:duration>\n" +
      "<itunes:keywords>Mongols, Genghis, Chingis, Jengiz, Khan, steppe, China, Eurasia, Russia, Persia, Turks, Huns, Asia, podcast, history, Middle Ages, invasion, conquest, Hungarians, Poland, Leignitz, Ogedai, Batu, Tartars, Muslim, Crusades, Cumans, Polovtsy, military</itunes:keywords>\n" +
      "\n" +
      "</item>\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "<item>\n" +
      "<title>Show 45 - Wrath of the Khans III</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha45_Wrath_of_the_Khans_III.mp3</guid>\n" +
      "<description>The expansion of Genghis Khan's conquests continue, with locations as far apart as Europe and China feeling the bloody effects of Mongol warfare and retribution. Can anything halt the carnage?</description>\n" +
      "<itunes:subtitle>The expansion of Genghis Khan's conquests continue, with locations as far apart as Europe and China feeling the bloody effects of Mongol warfare and retribution. Can anything halt the carnage?</itunes:subtitle>\n" +
      "<itunes:summary>The expansion of Genghis Khan's conquests continue, with locations as far apart as Europe and China feeling the bloody effects of Mongol warfare and retribution. Can anything halt the carnage?</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-45-wrath-of-the-khans-iii</link>\n" +
      "<pubDate>Sun, 23 Sep 2012 15:08:46 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha45_Wrath_of_the_Khans_III.mp3\" length=\"85299554\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>01:28:24</itunes:duration>\n" +
      "<itunes:keywords>Mongols, Genghis, Chingis, Jengiz, Khan, steppe, China, Eurasia, Russia, Persia, Turks, Huns, Central Asia, podcast, history, Middle Ages, invasion, conquest, Khwarezmian, Khwarezm, Shah, Islam, Muhammad, Muslim, Crusades, Cumans, Polovtsy</itunes:keywords>\n" +
      "\n" +
      "</item>\n" +
      "\n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\n" +
      "<item>\n" +
      "<title>Show 44 - Wrath of the Khans II</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha44_Wrath_of_the_Khans_II.mp3</guid>\n" +
      "<description>The Mongol leader Genghis Khan displays an unmatched level of strategic genius while moving against both Northern China and the Eastern Islamic world. Both civilizations are left stunned and millions are slaughtered.</description>\n" +
      "<itunes:subtitle>The Mongol leader Genghis Khan displays an unmatched level of strategic genius while moving against both Northern China and the Eastern Islamic world. Both civilizations are left stunned and millions are slaughtered.</itunes:subtitle>\n" +
      "<itunes:summary>The Mongol leader Genghis Khan displays an unmatched level of strategic genius while moving against both Northern China and the Eastern Islamic world. Both civilizations are left stunned and millions are slaughtered.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-44-wrath-of-the-khans-ii</link>\n" +
      "<pubDate>Tue, 31 Jul 2012 14:58:32 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha44_Wrath_of_the_Khans_II.mp3\" length=\"103193339\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>01:35:10</itunes:duration>\n" +
      "<itunes:keywords>Mongols, Genghis, Chingis, Jengiz, Khan, steppe, China, Eurasia, Russia, Persia, Turks, Huns, Central Asia, podcast, history, Middle Ages, Marco Polo, invasion, conquest, horsemen, Khwarezmian, Khwarezm, Shah, Islam, Muhammad, Muslim</itunes:keywords>\n" +
      "\n" +
      "</item>\t\t\t\n" +
      "\t\n" +
      "\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "<item>\n" +
      "<title>Show 43 - Wrath of the Khans I</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha43_Wrath_of_the_Khans_I.mp3</guid>\n" +
      "<description>In one of the most violent outbursts in history a little-known tribe of Eurasian nomads breaks upon the great societies of the Old World like a human tsunami. It may have ushered in the modern era, but at what cost?</description>\n" +
      "<itunes:subtitle>In one of the most violent outbursts in history a little-known tribe of Eurasian nomads breaks upon the great societies of the Old World like a human tsunami. It may have ushered in the modern era, but at what cost?</itunes:subtitle>\n" +
      "<itunes:summary>In one of the most violent outbursts in history a little-known tribe of Eurasian nomads breaks upon the great societies of the Old World like a human tsunami. It may have ushered in the modern era, but at what cost?</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-43-wrath-of-the-khans-i</link>\n" +
      "<pubDate>Wed, 13 Jun 2012 18:07:57 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha43_Wrath_of_the_Khans_I.mp3\" length=\"103193339\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>01:47:02</itunes:duration>\n" +
      "<itunes:keywords>Mongols, Genghis, Chingis, Jengiz, Khan, steppe, China, Eurasia, Russia, Persia, Turks, Huns, Central Asia, podcast, history, Middle Ages, Marco Polo, invasion, conquest, horsemen</itunes:keywords>\n" +
      "\n" +
      "</item>\t\t\t\t\t\t\t  \n" +
      "\t\t\t\t\t\t\t  \n" +
      "\n" +
      "<item>\n" +
      "<title>Show 42 - (BLITZ) Logical Insanity</title>\n" +
      "<guid>http://traffic.libsyn.com/dancarlinhh/dchha42__BLITZ_Logical_Insanity.mp3</guid>\n" +
      "<description>After many listener requests, Dan examines the issue of the morality of dropping the Atomic Bombs in the Second World War. As usual, he does so in his own unique, unexpected way.</description>\n" +
      "<itunes:subtitle>After many listener requests, Dan examines the issue of the morality of dropping the Atomic Bombs in the Second World War. As usual, he does so in his own unique, unexpected way.</itunes:subtitle>\n" +
      "<itunes:summary>After many listener requests, Dan examines the issue of the morality of dropping the Atomic Bombs in the Second World War. As usual, he does so in his own unique, unexpected way.</itunes:summary>\n" +
      "<link>http://www.dancarlin.com/product/hardcore-history-42-blitz-logical-insanity</link>\n" +
      "<pubDate>Sat, 31 Mar 2012 21:12:37 PST</pubDate>\n" +
      "<enclosure url=\"http://traffic.libsyn.com/dancarlinhh/dchha42__BLITZ_Logical_Insanity.mp3\" length=\"107582839\" type=\"audio/mpeg\" />\n" +
      "<itunes:duration>02:28:49</itunes:duration>\n" +
      "<itunes:keywords>Second World War, World War Two, World War One, First World War, Air Power, Atomic Bomb, Hiroshima, Nagasaki, nuclear weapons, strategic bombing, morality, civilian, casualties, war crimes, Luftwaffe, Blitz, firestorm, fire raid, Truman, Enola Gay</itunes:keywords>\n" +
      "\n" +
      "</item>\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "\n" +
      "</channel>\n" +
      "\n" +
      "</rss>";

  @Test
  public void foo() throws ParseException, DocumentException {

    /*
      PodcastTable.Podcast podcast = new PodcastTable.Podcast(new UUID(0,1), "title", "description", "", "");
      EpisodeSyncManager syncManager = new EpisodeSyncManager(podcast);
      int added = syncManager.addEpisodesFromResponse(response, podcast.id);
      Assert.assertEquals(added, 13);
      */
  }

}