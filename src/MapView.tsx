import React, {PureComponent} from "react";
import {MapViewProps, Region, LatLng, MarkerProps, Polygon} from "react-native-yandex-mapkit";
import {findNodeHandle, NativeModules, requireNativeComponent, UIManager, processColor} from "react-native";

const RNYandexMapKit = requireNativeComponent("RNYandexMapKit");
const RNYandexMapKitModule = NativeModules.RNYandexMapKit;

interface IProps extends MapViewProps<any, any> {}

export class MapView extends PureComponent<IProps> {
    private lastMarkers: MarkerProps<any>[] | undefined;
    private lastPolygons: Polygon<any>[] | undefined;
    private markers: MarkerProps<any>[] | undefined;
    private polygons: Polygon<any>[] | undefined;

    constructor(props: IProps) {
        super(props);
        this._onLocationSearch = this._onLocationSearch.bind(this);
        this._onMarkerPress = this._onMarkerPress.bind(this);
        this._onMapPress = this._onMapPress.bind(this);
        this._onPolygonPress = this._onPolygonPress.bind(this);
        this._onSuggestionsFetch = this._onSuggestionsFetch.bind(this);
        this._onDeviceLocationSearch = this._onDeviceLocationSearch.bind(this);
    }

    public static setApiKey(apiKey: string): void {
        RNYandexMapKitModule.setApiKey(apiKey);
    }

    public static setLocale(locale: string): void {
        RNYandexMapKitModule.setLocale(locale);
    }

    render(): JSX.Element {
        this.cacheProps();

        return (
            <RNYandexMapKit
                {...this.props}
                markers={this.markers}
                polygons={this.polygons}
                onLocationSearch={this._onLocationSearch}
                onMarkerPress={this._onMarkerPress}
                onMapPress={this._onMapPress}
                onPolygonPress={this._onPolygonPress}
                onDeviceLocationSearch={this._onDeviceLocationSearch}
                onSuggestionsFetch={this._onSuggestionsFetch}
            />
        );
    }

    private cacheProps(): void {
        const {polygons, markers, disableMarkers} = this.props;
        if (this.lastPolygons != polygons) {
            this.polygons = this.props.polygons && this.props.polygons.map(item => ({
                ...item,
                backgroundColor: processColor(item.backgroundColor) as any,
                borderColor: processColor(item.borderColor) as any,
            }));
        }
        this.lastPolygons = polygons;
        if (disableMarkers) {
            if (this.lastMarkers != markers) {
                this.markers = this.props.markers && this.props.markers.map(item => {
                    const {userData, ...markerProps} = item;

                    return markerProps;
                });
            } else {
                this.lastMarkers = markers;
            }
        } else {
            this.markers = markers;
        }
    }

    private _onSuggestionsFetch(event: any): void {
        this.props.onSuggestionsFetch && this.props.onSuggestionsFetch(event.nativeEvent);
    }

    private _onLocationSearch(event: any): void {
        this.props.onLocationSearch && this.props.onLocationSearch(event.nativeEvent);
    }

    private _onMarkerPress(event: any): void {
        this.props.onMarkerPress && this.props.onMarkerPress(event.nativeEvent);
    }

    private _onMapPress(event: any): void {
        this.props.onMapPress && this.props.onMapPress(event.nativeEvent);
    }

    private _onPolygonPress(event: any): void {
        this.props.onPolygonPress && this.props.onPolygonPress(event.nativeEvent);
    }

    private _onDeviceLocationSearch(event: any): void {
        this.props.onDeviceLocationSearch && this.props.onDeviceLocationSearch(event.nativeEvent);
    }

    public stopMapKit(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.stopMapKit,
            [],
        );
    }

    public navigateToUserLocation(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToUserLocation,
            [],
        );
    }

    public getUserLocation(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.getUserLocation,
            [],
        );
    }

    public zoomIn(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomIn,
            [],
        );
    }

    public zoomOut(): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.zoomOut,
            [],
        );
    }

    public navigateToRegion(region: Region, isAnimatedParam?: boolean): void {
        const isAnimated = isAnimatedParam ? isAnimatedParam : false;
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToRegion,
            [region, isAnimated],
        );
    }

    public navigateToBoundingBox(northEastPoint: LatLng, southWestPoint: LatLng): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.navigateToBoundingBox,
            [northEastPoint, southWestPoint],
        );
    }

    public fetchSuggestions(query: string): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.RNYandexMapKit.Commands.fetchSuggestions,
            [query],
        );
    }
}
