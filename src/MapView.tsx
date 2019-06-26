import React, {Component} from "react";
import {requireNativeComponent} from "react-native";
import {MapViewProps} from "react-native-yandex-mapkit";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");

export class MapView extends Component<MapViewProps> {
    render(): JSX.Element {
        return <RNYandexMapKit {...this.props}/>;
    }
}