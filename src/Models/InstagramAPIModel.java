package Models;

import InstagramTest.tester;
import org.jinstagram.Instagram;
import org.jinstagram.InstagramConfig;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.common.ImageData;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.media.MediaInfoFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.entity.users.feed.UserFeedData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

/**
 * Created by MatthewRowe on 4/25/15.
 */
public class InstagramAPIModel {

    private static String clientID;
    private static String clientSecret;
    private static Instagram instagram;
    private static LinkedList<String> imageURLlist;




    public static LinkedList<String> getNimageURLsFromArtist(int nImages, String artistName) {
        getKeyandSecret();
        setupInstagramObject();
        return getImages(nImages, artistName);
    }

                    private static void getKeyandSecret() {
                        try {
                            FileReader reader = new FileReader("../javaFinalProjectKeysAndSecrets/instagramKeys.txt");
                            BufferedReader bufReader = new BufferedReader(reader);
                            String line = bufReader.readLine();
                            String[] lineArray = line.split(",");
                            clientID = lineArray[0];
                            line = bufReader.readLine();
                            lineArray = line.split(",");
                            clientSecret = lineArray[0];
                            reader.close();
                        } catch  (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    private static void setupInstagramObject() {
                        instagram = new Instagram(clientID);
                    }

                    private static LinkedList<String> getImages (int nImages, String artistName) {
                        imageURLlist = new LinkedList<String>();
                        try {
                            UserFeed userFeed = instagram.searchUser(artistName);
                            List<UserFeedData> userFeedDataList = userFeed.getUserList();
                            UserFeedData userFeedData = userFeedDataList.get(0);    //currently no way in this API to know if got right user...
                            MediaFeed mediaFeed = instagram.getRecentMediaFeed(userFeedData.getId());
                            List<MediaFeedData> mediaFeedDataList  = mediaFeed.getData();
                            for (int ix = 0; ix < nImages; ix++) {
                                MediaFeedData mediaFeedData = mediaFeedDataList.get(ix);
                                Images images = mediaFeedData.getImages();
                                ImageData imageData = images.getStandardResolution();
                                imageURLlist.add(imageData.getImageUrl());
                            }
                        } catch (Exception e) {
                            System.out.printf("Couldn't get the images for this artist (InstagramAPIModel)");
                        }
                        return imageURLlist;
                    }

    public static String getLinkURL() {

        getKeyandSecret();

        try {
            UserFeed s1 = instagram.searchUser("kelly clarkson");
            List<UserFeedData> l1 = s1.getUserList();
            UserFeedData u = l1.get(0);
            MediaFeed mf =instagram.getRecentMediaFeed(u.getId());
            int canary = 1;
            List<MediaFeedData> mfdList  = mf.getData();
            MediaFeedData mfd = mfdList.get(0);

            Images images = mfd.getImages();
            ImageData id = images.getStandardResolution();
            System.out.println(id.getImageUrl());
            return id.getImageUrl();

        } catch (Exception e ) {

        }

        return null;

    }

    public static String getSecondLinkURL() {

        getKeyandSecret();
        Instagram instagram = new Instagram(clientID);
        try {
            UserFeed s1 = instagram.searchUser("kelly clarkson");
            List<UserFeedData> l1 = s1.getUserList();
            UserFeedData u = l1.get(0);
            MediaFeed mf =instagram.getRecentMediaFeed(u.getId());
            int canary = 1;
            List<MediaFeedData> mfdList  = mf.getData();
            MediaFeedData mfd = mfdList.get(1);

            Images images = mfd.getImages();
            ImageData id = images.getStandardResolution();
            System.out.println(id.getImageUrl());
            return id.getImageUrl();

        } catch (Exception e ) {

        }

        return null;

    }

    public static void main(String[] args) {
        new tester();
    }

}



/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */




