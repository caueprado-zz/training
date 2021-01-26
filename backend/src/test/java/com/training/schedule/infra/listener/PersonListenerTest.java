package com.training.schedule.infra.listener;


import com.training.schedule.TestBackground;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.service.PersonService;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonListenerTest extends TestBackground {

    private static final String DOCUMENT = "555.666.777.22";
    private static final String NAME = "James Kirk";
    @Autowired
    private PersonListener personListener;
    @MockBean
    private PersonService personService;
    @MockBean
    private DocumentClient documentClient;

    @Test
    public void whenAEmptyStringIsPassaed_ThenShouldNotCallPersonServiceToValidateDocument() {
        String emptyPersonId = "";
        personListener.onMessage(emptyPersonId);
        verify(personService, times(0)).validatePersonDocument(emptyPersonId);
    }

    @Test
    public void givenARecentlyRegisteredPerson_WhenMessageWithHisIdComesToValidate_ThenShouldValidate() {
        val expected = buildPerson(NAME, DOCUMENT, false);
        personListener.onMessage(expected.getId());
        verify(personService, times(1)).validatePersonDocument(expected.getId());
    }
}
