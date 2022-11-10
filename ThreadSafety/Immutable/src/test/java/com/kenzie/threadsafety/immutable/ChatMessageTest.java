package com.kenzie.threadsafety.immutable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatMessageTest {

    @Test
    public void getClassModifiers_isFinal() {
        //GIVEN
        Class c = ChatMessage.class;
        //WHEN
        boolean isFinal = Modifier.isFinal(c.getModifiers());
        //THEN
        Assertions.assertTrue(isFinal, "Immutable classes should not be extendable.");
    }

    @Test
    public void getFieldModifiers_areFinal() {
        //GIVEN
        Class p  = ChatMessage.class;
        int numFields = 2;
        //WHEN & THEN
        Assertions.assertEquals(numFields, p.getDeclaredFields().length);
        for (Field f : p.getDeclaredFields()) {
            Assertions.assertTrue(Modifier.isFinal(f.getModifiers()),
                    "Some fields were not declared final.");
        }
    }

    @Test
    public void getFieldModifiers_arePrivate() {
        //GIVEN
        Class p  = ChatMessage.class;
        int numFields = 2;
        //WHEN & THEN
        Assertions.assertEquals(numFields, p.getDeclaredFields().length);
        for (Field f : p.getDeclaredFields()) {
            Assertions.assertTrue(Modifier.isPrivate(f.getModifiers()),
                    "Some fields were not declared private.");
        }
    }

    @Test
    public void getMethods_retrieveClassFields_unchangedSinceConstruction() {
        //GIVEN
        Date now = new Date();
        ChatUser user1 = new ChatUser("UserName", "12345");
        ChatUser user2 = new ChatUser("UserName", "54321");
        ChatMessageContent messageContent = new ChatMessageContent(user1, "This is a test", now);
        ChatMessage cm = new ChatMessage(user2, messageContent);
        //WHEN
        //THEN
        Assertions.assertAll("Chat Message should be what was set",
            () -> assertEquals(user2, cm.getRecipient(), "Recipient was incorrect."),
            () -> assertEquals(messageContent, cm.getMessageContent(), "Message content was incorrect.")
        );
    }

}
