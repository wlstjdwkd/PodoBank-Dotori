import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, ScrollView } from 'react-native';

export default function ReceipeScreen({ route, navigation }) {
    // route.params에서 선택한 계좌와 명세서 번호(receipeSeq)를 가져옴
    const { selectedAccount, selectedReceipe } = route.params;

    // 가상 데이터 - 명세서 항목들
    const receipeItems = [
        { category: '식비', expense: '50,000원', savings: '20,000원' },
        { category: '주거', expense: '30,000원', savings: '10,000원' },
        // 다른 항목들 추가
    ];

    // 총계 계산
    const totalExpense = receipeItems.reduce((acc, item) => acc + parseInt(item.expense.replace(',', '')), 0);
    const totalSavings = receipeItems.reduce((acc, item) => acc + parseInt(item.savings.replace(',', '')), 0);

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

            {/* 선택한 명세서를 화면에 표시 */}
            <ScrollView contentContainerStyle={styles.contentContainer}>
                {/* 시민주의 통장 */}
                <Text style={styles.label}>시민주의 통장</Text>
                <Text style={styles.amount}>70,000원</Text>

                {/* 명세서 표 */}
                <View style={styles.receipeTable}>
                    {/* 제목 행 */}
                    <View style={styles.tableHeader}>
                        <Text style={styles.columnHeader}></Text>
                        <Text style={styles.columnHeader}>카테고리</Text>
                        <Text style={styles.columnHeader}>지출</Text>
                        <Text style={styles.columnHeader}>저축</Text>
                    </View>

                    {/* 항목들 */}
                    {receipeItems.map((item, index) => (
                        <View style={styles.tableRow} key={index}>
                            <Text style={styles.tableCell}>{index + 1}</Text>
                            <Text style={styles.tableCell}>{item.category}</Text>
                            <Text style={styles.tableCell}>{item.expense}</Text>
                            <Text style={styles.tableCell}>{item.savings}</Text>
                        </View>
                    ))}

                    {/* 구분선 */}
                    <View style={styles.tableSeparator} />

                    {/* 총계 행 */}
                    <View style={styles.tableRow}>
                        <Text style={[styles.tableCell, styles.totalCell]}>-</Text>
                        <Text style={[styles.tableCell, styles.totalCell]}>총계</Text>
                        <Text style={[styles.tableCell, styles.totalCell]}>{totalExpense}원</Text>
                        <Text style={[styles.tableCell, styles.totalCell]}>{totalSavings}원</Text>
                    </View>
                </View>
            </ScrollView>
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
    contentContainer: {
        flexGrow: 1, // 스크롤 가능하도록 설정
        alignItems: 'center',
        paddingBottom: 20, // 하단 여백
    },
    label: {
        fontSize: 24,
        textAlign: 'left',
        fontWeight: 'bold',
        marginBottom: 8,
        color: '#333',
    },
    amount: {
        fontSize: 40,
        textAlign: 'left',
        fontWeight: 'bold',
        marginBottom: 20,
        color: '#FF965C',
    },
    receipeTable: {
        width: '90%', // 화면 비율 90%
        borderWidth: 1,
        borderColor: '#DDD',
        borderRadius: 8,
        shadowColor: 'rgba(0, 0, 0, 0.2)',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.5,
        shadowRadius: 2,
        backgroundColor: 'white',
    },
    tableHeader: {
        flexDirection: 'row',
        backgroundColor: '#F5F5F5',
        borderBottomWidth: 1,
        borderColor: '#DDD',
    },
    columnHeader: {
        flex: 1,
        padding: 12,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    tableRow: {
        flexDirection: 'row',
        borderBottomWidth: 1,
        borderColor: '#DDD',
    },
    tableCell: {
        flex: 1,
        padding: 12,
        textAlign: 'center',
    },
    tableSeparator: {
        height: 1,
        backgroundColor: '#DDD',
    },
    totalCell: {
        fontWeight: 'bold',
        backgroundColor: '#F5F5F5',
    },
});
