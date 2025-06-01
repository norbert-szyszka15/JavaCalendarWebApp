package org.example.javacalendarwebapp.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationContextProviderTest {

    @BeforeEach
    void clearContext() throws Exception {
        java.lang.reflect.Field contextField = ApplicationContextProvider.class
                .getDeclaredField("context");
        contextField.setAccessible(true);
        contextField.set(null, null);
    }

    @Test
    void setAndGetBeanFromContext() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        ApplicationContextProvider provider = new ApplicationContextProvider();
        provider.setApplicationContext(mockContext);

        String beanValue = "dummy";
        when(mockContext.getBean(String.class)).thenReturn(beanValue);

        String result = ApplicationContextProvider.getBean(String.class);
        assertEquals("dummy", result);
        verify(mockContext, times(1)).getBean(String.class);
    }
}
