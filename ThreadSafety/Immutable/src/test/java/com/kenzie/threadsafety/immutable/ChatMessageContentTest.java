package com.kenzie.threadsafety.immutable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatMessageContentTest {

    @Test
    public void getClassModifiers_isFinal() {
        //GIVEN
        Class c = ChatMessageContent.class;
        //WHEN
        boolean isFinal = Modifier.isFinal(c.getModifiers());
        //THEN
        Assertions.assertTrue(isFinal, "Immutable classes should not be extendable.");
    }

    @Test
    public void getFieldModifiers_areFinal() {
        //GIVEN
        Class p  = ChatMessageContent.class;
        int numFields = 3;
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
        Class p  = ChatMessageContent.class;
        int numFields = 3;
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
        ChatUser user = new ChatUser("UserName", "12345");
        String testMessage = "This is a Test";
        ChatMessageContent messageContent = new ChatMessageContent(user, testMessage, now);
        //WHEN
        //THEN
        Assertions.assertAll("ChatMessageContent values should be what were set",
            () -> assertEquals(user, messageContent.getSender(), "User was incorrect."),
            () -> assertEquals(testMessage, messageContent.getMessage(), "The message was " +
                    "incorrect."),
            () -> assertEquals(now.getTime(), messageContent.getCreationDate().getTime(), "The creation date " +
                    "was incorrect.")
        );
    }

}
