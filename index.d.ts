/*tslint:disable:interface-name*/

declare module "react-native-yandex-mapkit" {
    import React, {Component} from "react";
    import {StyleProp, ViewStyle, NativeSyntheticEvent, ImageURISource, ImageRequireSource, ViewProps} from "react-native";

    export interface MapViewProps {
        markers?: MarkerProps[];
        polygons?: Polygon[];
        initialRegion?: Region;
        onMarkerPress?: (userData: Object, coordinates: LatLng) => void;
        // tslint:disable-next-line:no-any
        onMapPress?: (data: any) => void;
        // tslint:disable-next-line:no-any
        onLocationSearch?: (data: any) => void;
        followUserLocation?: boolean;
        style?: StyleProp<ViewStyle>;
    }

    export default class MapView extends Component<MapViewProps> {
        public static setApiKey(apiKey: string): void;
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

    export interface MapEvent<T = {}>
        extends NativeSyntheticEvent<
            T & {
            coordinate: LatLng;
            position: Point;
        }
            > {}

    export interface MarkerProps {
        identifier?: string;
        icon?: ImageURISource | ImageRequireSource;
        opacity?: number;
        coordinate: LatLng;
        centerOffset?: Point;
        calloutOffset?: Point;
        draggable?: boolean;
        userData?: object;
    }
}
