/*tslint:disable:interface-name*/

import {MarkerData} from "react-native-yandex-mapkit";

declare module "react-native-yandex-mapkit" {
    import React, {Component} from "react";
    import {StyleProp, ViewStyle, NativeSyntheticEvent, ImageURISource, ImageRequireSource, ViewProps} from "react-native";

    export interface MapViewProps {
        markers?: MarkerProps[];
        polygons?: Polygon[];
        initialRegion?: Region;
        onMarkerPress?: (data: MarkerData) => void;
        onMapPress?: (data: any) => void;
        onLocationSearch?: (data: any) => void;
        searchLocation?: boolean;
        searchRoute?: boolean;
        style?: StyleProp<ViewStyle>;
    }

    export default class MapView extends Component<MapViewProps> {
        public static setApiKey(apiKey: string): void;
        public static setLocale(locale: string): void;
        public animateToRegion(region: Region): void;
        public zoomIn(): void;
        public zoomOut(): void;
        public navigateToUserLocation(): void;
    }

    export interface Polygon {
        points: LatLng[];
    }

    export interface Region {
        latitude: number;
        longitude: number;
        latitudeDelta: number;
        longitudeDelta: number;
    }

    export interface LatLng {
        latitude: number;
        longitude: number;
    }

    export interface Point {
        x: number;
        y: number;
    }

    export interface MarkerProps {
        identifier?: string;
        icon?: "pin" | "selectedPin" | "user";
        opacity?: number;
        coordinate: LatLng;
        centerOffset?: Point;
        calloutOffset?: Point;
        draggable?: boolean;
        userData?: object;
    }

    export interface MarkerData extends LatLng {
        id?: string;
    }
}
