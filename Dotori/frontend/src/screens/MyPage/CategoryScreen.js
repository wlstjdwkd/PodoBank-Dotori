import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, FlatList } from 'react-native';

const categorys = [
    {
        categorySeq: '1',
        categoryTitle: "옷(겨울)",
    },
    {
        categorySeq: '2',
        categoryTitle: "요가학원",
    },
    {
        categorySeq: '3',
        categoryTitle: "가구구매",
    },
    {
        categorySeq: '4',
        categoryTitle: "식자재",
    },
    {
        categorySeq: '5',
        categoryTitle: "배달",
    },
];


const randomColors = [
    '#FFD700', '#FFA07A', '#FFC0CB', '#90EE90', '#87CEEB', '#FFB6C1', '#FF1493', '#DDA0DD',
    '#F0E68C', '#00CED1', '#FF6347', '#E0FFFF', '#F08080', '#FFDAB9', '#D8BFD8'
];
        

export default function CategoryScreen({ navigation }) {
    const renderItem = ({ item }) => (
        <TouchableOpacity
            style={[
                styles.categoryItem,
                { backgroundColor: randomColors[Math.floor(Math.random() * randomColors.length)] },
            ]}
            onPress={() => navigation.navigate('CategorySettingScreen', { categorySeq: item.categorySeq })}
        >
            <Text style={styles.categoryTitle}>{item.categoryTitle}</Text>
            <Text style={styles.categoryGroup}>{item.categoryGroup}</Text>
            <Image
                style={styles.arrowIcon}
                source={require('../../assets/icon/forward_arrow.png')} // 화살표 아이콘 이미지 경로
            />
        </TouchableOpacity>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                {/* 뒤로가기 버튼 */}
                <TouchableOpacity onPress={() => navigation.goBack()}>
                    <Image
                        style={styles.arrowIcon}
                        source={require('../../assets/icon/back_arrow.png')} // 화살표 아이콘 이미지 경로
                    />
                </TouchableOpacity>

                {/* 카테고리 보기 텍스트 */}
                <Text style={styles.CategoryText}>카테고리 보기</Text>
            </View>

            {/* 상단 작은 사각형 4개 */}
            <View style={styles.titleContainer}>
                <View style={styles.rectangleContainer}>
                    <View style={styles.smallRectangles}>
                        <View style={[styles.smallRectangle, { backgroundColor: '#FFEEAD' }]}/>
                        <View style={[styles.smallRectangle, { backgroundColor: '#D9534F' }]}/>
                    </View>
                    <View style={styles.smallRectangles}>
                        <View style={[styles.smallRectangle, { backgroundColor: '#96CEB4' }]}/>
                        <View style={[styles.smallRectangle, { backgroundColor: '#FFAD60' }]}/>
                    </View>
                </View>
                <Text style={styles.titleText}>카테고리 </Text>
            </View>

            {/* 카테고리 목록 */}
            <FlatList
                data={categorys}
                renderItem={renderItem}
                keyExtractor={(item) => item.categorySeq}
                contentContainerStyle={styles.categoryList}
            />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'white',
        padding: 16,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 16,
        marginTop: 15,
    },
    arrowIcon: {
        width: 20,
        height: 20,
    },
    CategoryText: {
        fontSize: 24,
        flex: 1,
        textAlign: 'center',
    },
    titleContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginLeft: '10%',
        marginTop: 30,
        marginBottom: 30,
    },
    smallRectangles: {
        alignItems: 'center',
        justifyContent: 'space-between', // 가로 간격을 균등하게 분배
        marginBottom: 10,
    },
    smallRectangle: {
        width: 20,
        height: 20,
        backgroundColor: 'red', // 임시 색상
        borderRadius: 8,
        justifyContent: 'center',
        alignItems: 'center',
        margin: 1,
    },
    categoryLabel: {
        fontSize: 14,
        fontWeight: 'bold',
        marginLeft: 10,
    },
    rectangleContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 10,
        marginRight: 10,
        marginTop: 10,
    },
    titleText: {
        fontSize: 30,
        marginTop: -10
    },
    categoryList: {
        alignItems: 'center', // 중앙 정렬
        width: '80%', // 화면 가로폭의 80%
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    categoryItem: {
        flexDirection: 'row',
        alignItems: 'center',
        padding: 12,
        marginBottom: 15,
        borderRadius: 28,
        height: 50,
        width: '100%', // 카테고리 아이템 가로폭을 100%로 설정
    },
    categoryTitle: {
        fontSize: 16,
        marginRight: 'auto',
        marginLeft: '5%',
    },
    arrowIcon: {
        width: 20,
        height: 20,
    },
});
