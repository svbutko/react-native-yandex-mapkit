import React, {Component} from "react";
import {requireNativeComponent} from "react-native";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");

export class MapView extends Component {
    render(): JSX.Element {
        return <RNYandexMapKit style={{flex: 1, backgroundColor: "green"}}/>;
    }
}