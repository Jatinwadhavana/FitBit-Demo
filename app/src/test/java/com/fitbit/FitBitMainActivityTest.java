package com.fitbit;


import com.demo.fitbit.fitbitactivity.FitBitActViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class FitBitMainActivityTest {

    @Mock
    private FitBitActViewModel fitBitActViewModel;

    @Before
    public void setUp() {
        fitBitActViewModel = new FitBitActViewModel();
    }

    @Test
    public void checkAccessToken() {
        when(fitBitActViewModel.getFitBitUserAccess()).thenReturn("");
    }
}