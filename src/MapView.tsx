import React, {Component} from "react";
import {requireNativeComponent} from "react-native";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");

export class MapView extends Component {
    render(): JSX.Element {
        return <RNYandexMapKit {...this.props}/>;
    }
}