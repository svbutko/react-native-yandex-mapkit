/*tslint:disable:interface-name*/
declare module "react-native-yandex-mapkit" {
    import React, {Component, ClassAttributes} from "react";
    import {StyleProp, ViewStyle} from "react-native";

    export interface MapViewProps {
        style?: StyleProp<ViewStyle>;
    }

    export interface MapViewStatic extends Component<MapViewProps> {
    }

    export type MapViewProperties = MapViewProps & ClassAttributes<MapViewStatic>;

    export default class MapView extends Component<MapViewProperties> {
    }
}
