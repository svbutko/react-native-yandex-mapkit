import React, {Component} from "react";
import {requireNativeComponent, NativeModules} from "react-native";
import {MapViewProps} from "react-native-yandex-mapkit";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

export class MapView extends Component<MapViewProps> {
    public setApiKey = (apiKey: string): void => {
        RNYandexMapKitModule.setApiKey(apiKey);
    };

    render(): JSX.Element {
        return <RNYandexMapKit {...this.props}/>;
    }
}