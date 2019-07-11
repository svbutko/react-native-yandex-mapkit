import React, {Component} from "react";
import {MapViewProps, Region} from "react-native-yandex-mapkit";
import {findNodeHandle, NativeModules, requireNativeComponent, UIManager} from "react-native";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export class MapView extends Component<MapViewProps> {
    constructor(props: MapViewProps) {
        super(props);
        this._onLocationSearch = this._onLocationSearch.bind(this);
        this._onMarkerPress = this._onMarkerPress.bind(this);
        this._onMapPress = this._onMapPress.bind(this);
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

    public navigateToRegion(region: Region, isAnimated?: boolean): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToRegion,
            [region, isAnimated],
        );
    }
}