import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { Picker } from '@react-native-picker/picker';

const accounts = [
    {
        accountNumber: '0000000000001',
        accountName: '프론트'
    },
    {
        accountNumber: '0000000000002',
        accountName: '너무'
    },
    {
        accountNumber: '0000000000003',
        accountName: '힘들어'
    },
];

const recepies = [
    {
        receipeSeq: '1',
        receipeStartAt: '2023.09.01',
        receipeEndAt: '2023.09.10',
    },
    {
        receipeSeq: '2',
        receipeStartAt: '2023.08.01',
        receipeEndAt: '2023.08.10',
    },
    {
        receipeSeq: '3',
        receipeStartAt: '2023.07.15',
        receipeEndAt: '2023.07.25',
    },
];

export default function ReceipeSelectScreen({ navigation }) {
    const [selectedAccount, setSelectedAccount] = useState(accounts[0].accountName);
    const [selectedReceipe, setSelectedReceipe] = useState(recepies[0].receipeSeq);

    const handleViewReceipe = () => {
        // 명세서 보기 화면으로 이동하면서 선택한 계좌와 명세서 번호(receipeSeq)를 전달
        navigation.navigate("ReceipeScreen", {
            selectedAccount,
            selectedReceipe,
        });
    };

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
                {/* 명세서 보기 텍스트 */}
                <Text style={styles.selectReceipeText}>명세서 보기</Text>
            </View>
            
            <View style={styles.container}>
                {/* 명세서 선택 텍스트 */}
                <Text style={styles.descriptionText}>명세서의 기간을 선택해 주세요</Text>

                {/* 계좌 이름과 기간 Picker를 가로로 배치 */}
                <View style={styles.pickerContainer}>
                    <View style={styles.pickerItem}>
                        <Text style={styles.dropdownLabel}>계좌 이름</Text>
                        <Picker
                            style={styles.dropdownPicker}
                            selectedValue={selectedAccount}
                            onValueChange={(itemValue) => setSelectedAccount(itemValue)}
                        >
                            {/* 계좌 이름 목록 */}
                            {accounts.map((account, index) => (
                                <Picker.Item
                                    key={index}
                                    label={account.accountName}
                                    value={account.accountName}
                                />
                            ))}
                        </Picker>
                    </View>
                    <View style={styles.pickerItem}>
                        <Text style={styles.dropdownLabel}>기간</Text>
                        <Picker
                            style={styles.dropdownPicker}
                            selectedValue={selectedReceipe}
                            onValueChange={(itemValue, itemIndex) => {
                                setSelectedReceipe(itemValue);
                            }}
                        >
                            {/* 기간 목록 */}
                            {recepies.map((receipe, index) => (
                                <Picker.Item
                                    key={index}
                                    label={`${receipe.receipeStartAt} - ${receipe.receipeEndAt}`}
                                    value={receipe.receipeSeq}
                                />
                            ))}
                        </Picker>
                    </View>
                </View>
                {/* 명세서 보기 버튼 */}
                <TouchableOpacity style={styles.viewReceipeButton} onPress={handleViewReceipe}>
                    <Text style={styles.viewReceipeButtonText}>명세서 보기</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
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
    selectReceipeText: {
        fontSize: 24,
        flex: 1,
        textAlign: 'center',
        fontWeight: 'bold', // 텍스트를 굵게 설정
    },
    container: {
        flex: 1,
        backgroundColor: 'white',
        padding: 16,
        justifyContent: 'center', // 수직 중앙 정렬
    },
    descriptionText: {
        fontSize: 20,
        textAlign: 'left',
        fontWeight: 'bold', // 텍스트를 굵게 설정
        marginBottom: 40,
        marginLeft: '5%',
    },
    pickerContainer: {
        flexDirection: 'row', // 가로로 배치
        justifyContent: 'space-between', // 간격을 균등하게 분배
        marginBottom: 20,
        marginHorizontal: '5%',
    },
    pickerItem: {
        flex: 1, // 1:1 비율로 공간을 분배
        marginRight: 8, // 간격 설정
    },
    dropdownLabel: {
        fontSize: 16,
        fontWeight: 'bold', // 라벨 텍스트 굵게 설정
    },
    dropdownPicker: {
        height: 40,
        borderWidth: 1,
        borderColor: '#7B7B7B', // 테두리 색상 설정
        borderRadius: 8,
    },
    viewReceipeButton: {
        width: '80%',
        backgroundColor: '#FF965C',
        borderRadius: 8,
        padding: 12,
        alignSelf: 'center',
        alignItems: 'center',
        marginVertical: 20,
    },
    viewReceipeButtonText: {
        color: 'white', // 흰색 텍스트
        fontWeight: 'bold', // 텍스트 굵게 설정
        fontSize: 16,
    },
});
