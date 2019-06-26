import React, {Component} from "react";
import {NativeModules, requireNativeComponent} from "react-native";
import {MapViewProps} from "react-native-yandex-mapkit";

const RNYandexMapKit = requireNativeComponent("RNYandexMapView");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export class MapView extends Component<MapViewProps> {
    public static setApiKey(apiKey: string): void {
        console.log("KEY: ", apiKey);
        console.log("RNYandexMapKitModule: ", RNYandexMapKitModule);
        RNYandexMapKitModule.setApiKey(apiKey);
    }

    render(): JSX.Element {
        return <RNYandexMapKit {...this.props}/>;
    }
}