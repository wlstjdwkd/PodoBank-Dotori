import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, FlatList } from 'react-native';

const categorys = [
    {
        categorySeq: '1',
        categoryTitle: "옷(겨울)",
        categoryList: ['마왕족발', '김치찜은 못참치', '최각루', '피자가좋다', '세찜', '치킨과피자가같이 이게', '내가닭이다'],
        newCategoryList: ['신촌 등갈비찜']
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

export default function CategorySettingScreen({ navigation, route }) {
    // route.params를 통해 이전 페이지에서 받은 categorySeq를 가져옵니다.
    const { categorySeq } = route.params;

    // categorySeq와 일치하는 category 데이터를 찾습니다.
    const category = categorys.find(item => item.categorySeq === categorySeq);

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
                
            </View>

            <View style={styles.topContainer}>
                {/* 페이지 상단 좌측에 categoryTitle 출력 */}
                <View style={styles.categoryTitleContainer}>
                    <Text style={styles.categoryTitle}>{category.categoryTitle}</Text>
                    <Text style={styles.categoryTitleExplain}>자주 사용하는 사용처를 분류해주세요.</Text>
                </View>
                <Image
                        style={styles.checkIcon}
                        source={require('../../assets/images/Hamster/CheckHamster.png')}
                    />
            </View>    

            {/* 자주 사용하는 사용처 */}
            <View style={styles.categoryGroup}>
                <Text style={styles.categoryGroupText}>자주 사용하는 사용처</Text>
            </View>
            {/* 카테고리 목록 */}
            <FlatList
                data={category.categoryList}
                renderItem={({ item }) => (
                    <View style={styles.categoryItem}>
                        <Text style={styles.categoryItemText}>{item}</Text>
                    </View>
                )}
                keyExtractor={(item, index) => index.toString()}
                contentContainerStyle={styles.categoryList}
                numColumns={3} // 2개의 열로 배치
            />
            {/* 구분선 */}
            <View style={styles.separator}></View>
            {/* 신규 사용처 */}
            <View style={styles.categoryGroup}>
                <Text style={styles.categoryGroupText}>신규 사용처</Text>
            </View>
            {/* 신규 카테고리 목록 */}
            <FlatList
                data={category.newCategoryList}
                renderItem={({ item }) => (
                    <View style={styles.categoryItem}>
                        <Text style={styles.categoryItemText}>{item}</Text>
                    </View>
                )}
                keyExtractor={(item, index) => index.toString()}
                contentContainerStyle={styles.categoryList}
                numColumns={2} // 2개의 열로 배치
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
    topContainer: {
        flexDirection: 'row',
        marginLeft: "5%",
        marginTop: 20,
        marginBottom: 50,
    },
    categoryTitleContainer: {
        flexDirection: 'column',
    },
    categoryTitle: {
        fontSize: 30,
        flex: 1,
        textAlign: 'left',
        marginLeft: 16, // categoryTitle을 뒤에 추가
        marginTop: 20,
    },
    categoryTitleExplain: {
        fontSize: 10,
        flex: 1,
        textAlign: 'left',
        marginLeft: 16, // categoryTitle을 뒤에 추가
        color: "#7B7B7B"
    },
    categoryGroup: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 10,
    },
    categoryGroupText: {
        fontSize: 23,
        fontWeight: 'bold',
        marginLeft: '5%',
        marginBottom: 10,
    },
    checkIcon: {
        width: 150,
        height: 150,
        marginLeft: 10,
    },
    categoryList: {
        flexDirection: 'column', // 세로로 배치
        alignSelf: 'left'
    },
    categoryItem: {
        alignItems: 'center',
        padding: 2,
        marginBottom: 8,
        marginHorizontal: 8, // 좌우 여백 추가
        borderWidth: 1, // 테두리 추가
        borderColor: 'black', // 테두리 색상
        borderRadius: 20, // 테두리 둥글게 설정
    },
    categoryItemText: {
        fontSize: 16,
        marginHorizontal: 5,
    },
    separator: {
        height: 2,
        backgroundColor: '#BAC0CA', // 구분선 색상 변경
        marginVertical: 20,
    },
});