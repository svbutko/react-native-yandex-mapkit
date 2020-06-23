/*tslint:disable:interface-name*/

declare module "react-native-yandex-mapkit" {
    import React, {Component} from "react";
    import {StyleProp, ViewStyle} from "react-native";

    export interface MapViewProps<T, B> {
        markers?: MarkerProps<T>[];
        polygons?: Polygon<B>[];
        initialRegion?: Region;
        onMarkerPress?: (data: MarkerData<T>) => void;
        onPolygonPress?: (data: MarkerData<B>) => void;
        onMapPress?: (data: any) => void;
        onLocationSearch?: (data: any) => void;
        onSuggestionsFetch?: (data: SuggestionsResult) => void;
        onDeviceLocationSearch?: (data: DeviceLocation) => void;
        searchLocation?: boolean;
        searchRoute?: MarkerProps[];
        searchMarker?: MarkerProps;
        style?: StyleProp<ViewStyle>;
        boundingBox?: BoundingBox;
        disableMarkers?: boolean;
    }

    export default class MapView<T, B> extends Component<Partial<MapViewProps<T, B>>> {
        public static setApiKey(apiKey: string): void;
        public static setLocale(locale: string): void;
        public navigateToRegion(region: Region, isAnimated?: boolean): void;
        public navigateToBoundingBox(northEastPoint: LatLng, southWestPoint: LatLng): void;
        public zoomIn(): void;
        public zoomOut(): void;
        public navigateToUserLocation(): void;
        public getUserLocation(): void;
        public fetchSuggestions(query: string, searchCoordinates?: SearchCoordinates): void;
        public stopMapKit(): void;
    }

    export interface BoundingBox {
        northEastPoint: LatLng;
        southWestPoint: LatLng;
    }

    export interface Polygon<B> {
        userData?: B;
        points: LatLng[];
        backgroundColor?: string;
        borderColor?: string;
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

    export interface MarkerProps<T> {
        icon?: "pin" | "selectedPin" | "user" | "disabled";
        opacity?: number;
        coordinate: LatLng;
        centerOffset?: Point;
        calloutOffset?: Point;
        draggable?: boolean;
        userData?: T;
    }

    export interface SuggestionsResult {
        suggestions: Suggestion[];
    }

    export interface Suggestion {
        value: string;
        searchText: string;
    }

    export interface MarkerData<B> extends LatLng {
        data?: B;
    }

    export interface SearchCoordinates {
        southWest: LatLng;
        northEast: LatLng;
    }

    export interface DeviceLocation extends LatLng {
        location: string;
        descriptionLocation?: string;
    }
}
