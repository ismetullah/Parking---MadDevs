package com.ismet.parkingzonemaddevs;

import com.google.android.gms.maps.model.LatLng;
import com.ismet.parkingzonemaddevs.data.DataManager;
import com.ismet.parkingzonemaddevs.data.model.Corner;
import com.ismet.parkingzonemaddevs.data.model.CurrentParking;
import com.ismet.parkingzonemaddevs.data.model.LastEnteredZone;
import com.ismet.parkingzonemaddevs.data.model.ParkingZone;
import com.ismet.parkingzonemaddevs.rx.TestSchedulerProvider;
import com.ismet.parkingzonemaddevs.ui.location.LocationTrackerContract;
import com.ismet.parkingzonemaddevs.ui.location.LocationTrackerPresenter;
import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestA {
    @Mock
    public LocationTrackerContract.View view;
    private LocationTrackerPresenter presenter;

    @Mock
    public DataManager dataManager;
    private TestScheduler mTestScheduler;

    private LatLng mkr11Location = new LatLng(42.834272, 74.600603);
    private ParkingZone parkingZone = new ParkingZone("11 MKR",
            11, 9, "#008000", getPolygonCorners(), new ArrayList<>());
    private LastEnteredZone lastEnteredZone11Mkr = new LastEnteredZone(1556368762486L, "11 MKR");

    private ArrayList<Corner> getPolygonCorners() {
        ArrayList<Corner> res = new ArrayList<>();
        res.add(new Corner(42.834238, 74.600115));
        res.add(new Corner(42.834480, 74.600127));
        res.add(new Corner(42.834439, 74.600919));
        res.add(new Corner(42.834197, 74.600951));
        return res;
    }

    @Before
    public void setup() {

        ArrayList<ParkingZone> parkingZones = new ArrayList<>();
        parkingZones.add(parkingZone);
        Observable<ArrayList<ParkingZone>> observable = Observable.just(parkingZones);
        // somehow after this, presenter's parkingzones is empty
        doReturn(observable)
                .when(dataManager)
                .getAllParkingZones();

        mTestScheduler = new TestScheduler();
        TestSchedulerProvider testSchedulerProvider = new TestSchedulerProvider(mTestScheduler);
        presenter = new LocationTrackerPresenter(dataManager, testSchedulerProvider);
        presenter.bindView(view);
    }

    @After
    public void tearDown() throws Exception {
        mTestScheduler = null;
        presenter = null;
        view = null;
    }

    @Test
    public void testAsksToPark() {
        doReturn(lastEnteredZone11Mkr)
                .when(dataManager)
                .getLastEnteredZone();

        CurrentParking currentParking = new CurrentParking(false, true);
        doReturn(currentParking)
                .when(dataManager)
                .getCurrentParking();

        doReturn(true)
                .when(dataManager)
                .getCanAskToPark();

        presenter.onLocationChanged(mkr11Location);
        verify(view).notifyToPark(parkingZone);
    }

}
