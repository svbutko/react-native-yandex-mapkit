import React, {Component} from "react";
import {MapViewProps, Region} from "react-native-yandex-mapkit";
import {UIManager, requireNativeComponent, findNodeHandle, NativeModules} from "react-native";

const RNYandexMapKit = requireNativeComponent("RNYandexMapView");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export class MapView extends Component<MapViewProps> {
    public static setApiKey(apiKey: string): void {
        RNYandexMapKitModule.setApiKey(apiKey);
    }

    render(): JSX.Element {
        return <RNYandexMapKit {...this.props}/>;
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

    public zoomOut(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomOut,
            [],
        );
    }

    public animateToRegion(region: Region): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.animateToRegion,
            [region],
        );
    }
}