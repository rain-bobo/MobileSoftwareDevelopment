package com.casper.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.casper.test.data.ShopLoader;
import com.casper.test.data.model.Shop;

import java.util.ArrayList;

public class MapViewFragment extends Fragment {

    public MapViewFragment() {
        // Required empty public constructor
    }

    private MapView mapView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView=(MapView) view.findViewById(R.id.bmapView);

        BaiduMap baiduMap = mapView.getMap();
        //修改百度地图的初始位置
        LatLng centerPoint = new LatLng(22.2559,113.541112);
        MapStatus mMapStatus = new MapStatus.Builder().target(centerPoint).zoom(17).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);

        //添加标记点
        //准备 marker 的图片
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon);
        //准备 marker option 添加 marker 使用
        MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(centerPoint);
        //获取添加的 marker 这样便于后续的操作
        Marker marker = (Marker) baiduMap.addOverlay(markerOption);

        //添加文字
        OverlayOptions textOption = new TextOptions().bgColor(0x77FFFFF0).fontSize(35)
                .fontColor(0xFF000080).text("暨南大学珠海校区").rotate(0).position(centerPoint);
        baiduMap.addOverlay(textOption);

        //响应事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                Toast.makeText(getContext(),"图标被点击",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        final ShopLoader shopLoader = new ShopLoader();

        Handler handler = new Handler(){
            public void handleMessage(Message msg){
                drawShops(shopLoader.getShops());
            };
        };
        shopLoader.load(handler,"http://file.nidama.net/class/mobile_develop/data/bookstore.json");

        return view;
    }


    void drawShops(ArrayList<Shop>shops){
        if(mapView==null)
            return;
        BaiduMap Baidumap = mapView.getMap();
        for(int i=0;i<shops.size();i++){
            Shop shop = shops.get(i);
            //设定中心坐标
            LatLng cenpt = new LatLng(shop.getLatitude(),shop.getLongitude());

            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon);
            MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(cenpt);
            Marker marker = (Marker) Baidumap.addOverlay(markerOption);

            OverlayOptions textOption = new TextOptions().bgColor(0x0F0FF0).fontSize(35)
                    .fontColor(0x00ff00ff).text(shop.getName()).rotate(0).position(cenpt);
            Baidumap.addOverlay(textOption);


        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if(mapView!=null)
            mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(mapView!=null)
            mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mapView!=null)
            mapView.onDestroy();
    }
}