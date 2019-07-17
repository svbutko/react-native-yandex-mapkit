import React, {Component} from "react";
import {MapViewProps, Region, LatLng} from "react-native-yandex-mapkit";
import {findNodeHandle, NativeModules, requireNativeComponent, UIManager} from "react-native";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export class MapView extends Component<MapViewProps> {
    constructor(props: MapViewProps) {
        super(props);
        this._onLocationSearch = this._onLocationSearch.bind(this);
        this._onMarkerPress = this._onMarkerPress.bind(this);
        this._onMapPress = this._onMapPress.bind(this);
        this._onPolygonPress = this._onPolygonPress.bind(this);
    }

    public static setApiKey(apiKey: string): void {
        RNYandexMapKitModule.setApiKey(apiKey);
    }

    public static setLocale(locale: string): void {
        RNYandexMapKitModule.setLocale(locale);
    }

    render(): JSX.Element {
        return (
            <RNYandexMapKit
                {...this.props}
                onLocationSearch={this._onLocationSearch}
                onMarkerPress={this._onMarkerPress}
                onMapPress={this._onMapPress}
                onPolygonPress={this._onPolygonPress}
            />
        );
    }

    private _onLocationSearch(event: any): void {
        this.props.onLocationSearch && this.props.onLocationSearch(event.nativeEvent);
    }

    private _onMarkerPress(event: any): void {
        this.props.onMarkerPress && this.props.onMarkerPress(event.nativeEvent);
    }

    private _onMapPress(event: any): void {
        this.props.onMapPress && this.props.onMapPress(event.nativeEvent);
    }

    private _onPolygonPress(event: any): void {
        this.props.onPolygonPress && this.props.onPolygonPress(event.nativeEvent);
    }

    public navigateToUserLocation(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToUserLocation,
            [],
        );
    }

    public zoomIn(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomIn,
            [],
        );
    }

    public zoomOut(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomOut,
            [],
        );
    }

    public navigateToRegion(region: Region, isAnimatedParam?: boolean): void {
        const isAnimated = isAnimatedParam ? isAnimatedParam : false;
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToRegion,
            [region, isAnimated],
        );
    }

    public navigateToBoundingBox(northEastPoint: LatLng, southWestPoint: LatLng): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToBoundingBox,
            [northEastPoint, southWestPoint],
        );
    }
}