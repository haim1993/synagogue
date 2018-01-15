package com.example.shlez.synagogue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Yedidya on 1/14/2018.
 */
@RunWith(MockitoJUnitRunner.class)

public class MockitoTest {

        private static final String FAKE_STRING = "RevavaMockitoTest@app.com";

        @Mock
        Context mMockContext;

        @Test
        public void readStringFromContext_LocalizedString()
        {
            // Given a mocked Context injected into the object under test...
//            when(mMockContext.getString(R.string.hello_world))
//                    .thenReturn(FAKE_STRING);
            Prayer myObjectUnderTest = new Prayer();
            myObjectUnderTest.setAddress("Revava");
            myObjectUnderTest.setEmail("MockitoTest@app.com");
            myObjectUnderTest.setName("Yedidya");
            myObjectUnderTest.setPhone("0526458026");


            // ...when the string is returned from the object under test...
            String result = myObjectUnderTest.getAddress() + myObjectUnderTest.getEmail();

            // ...then the result should be the expected one.
            assertThat(result, is(FAKE_STRING));



        }

    @Test
    public void passwordValidationCheck()
    {
        // Given a mocked Context injected into the object under test...
//            when(mMockContext.getString(R.string.hello_world))
//                    .thenReturn(FAKE_STRING);
        Prayer myObjectUnderTest = new Prayer();


        // ...when the string is returned from the object under test..

        // ...then the result should be the expected one.
        //  assertThat(result, is(FAKE_STRING));


        assertThat( myObjectUnderTest.isValidPasswordTest("123467"),is(true));
        assertThat( myObjectUnderTest.isValidPasswordTest("1swsadsade7"),is(true));
        assertThat( myObjectUnderTest.isValidPasswordTest("1h7"),is(false));
        assertThat( myObjectUnderTest.isValidPasswordTest(""),is(false));
        assertThat( myObjectUnderTest.isValidPasswordTest("227kfh3k2"),is(true));



     }



    }


