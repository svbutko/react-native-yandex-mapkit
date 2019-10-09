/*tslint:disable:interface-name*/

declare module "react-native-yandex-mapkit" {
    import React, {Component} from "react";
    import {StyleProp, ViewStyle} from "react-native";

    export interface MapViewProps {
        markers?: MarkerProps[];
        polygons?: Polygon[];
        initialRegion?: Region;
        onMarkerPress?: (data: MarkerData) => void;
        onPolygonPress?: (data: MarkerData) => void;
        onMapPress?: (data: any) => void;
        onLocationSearch?: (data: any) => void;
        onSuggestionsFetch?: (data: SuggestionsResult) => void;
        searchLocation?: boolean;
        searchRoute?: MarkerProps[];
        searchMarker?: MarkerProps;
        style?: StyleProp<ViewStyle>;
        boundingBox?: BoundingBox;
    }

    export default class MapView extends Component<Partial<MapViewProps>> {
        public static setApiKey(apiKey: string): void;
        public static setLocale(locale: string): void;
        public navigateToRegion(region: Region, isAnimated?: boolean): void;
        public navigateToBoundingBox(northEastPoint: LatLng, southWestPoint: LatLng): void;
        public zoomIn(): void;
        public zoomOut(): void;
        public navigateToUserLocation(): void;
        public fetchSuggestions(query: string): void;
        public stopMapKit(): void;
    }

    export interface BoundingBox {
        northEastPoint: LatLng;
        southWestPoint: LatLng;
    }

    export interface Polygon {
        identifier?: string;
        userData?: object;
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
        icon?: "pin" | "selectedPin" | "user" | "disabled";
        opacity?: number;
        coordinate: LatLng;
        centerOffset?: Point;
        calloutOffset?: Point;
        draggable?: boolean;
        userData?: object;
    }

    export interface SuggestionsResult {
        suggestions: Suggestion[];
    }

    export interface Suggestion {
        value: string;
    }

    export interface MarkerData extends LatLng {
        id?: string;
    }
}
