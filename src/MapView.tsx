import React, {Component} from "react";
import {requireNativeComponent} from "react-native";
import {MapViewProps} from "react-native-yandex-mapkit";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");

export class MapView extends Component<MapViewProps> {
    public setApiKey = (apiKey: string): void => {
        RNYandexMapKit.setApiKey(apiKey);
    };

    render(): JSX.Element {
        return <RNYandexMapKit {...this.props}/>;
    }
}