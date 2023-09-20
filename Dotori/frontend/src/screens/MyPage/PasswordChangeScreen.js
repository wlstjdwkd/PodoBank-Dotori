import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, TextInput, Button } from 'react-native';

export default function PasswordChangeScreen({ navigation }) {
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

                {/* 비밀번호 변경 텍스트 */}
                <Text style={styles.passwordChangeText}>비밀번호 변경</Text>
            </View>
            
            <View style={styles.iconContainer}>
                <Image
                    style={styles.lockIcon}
                    source={require('../../assets/icon/lock.png')} // 프로필 이미지 경로
                />
            </View>

            <View style={styles.passwordInput}>
                <Text style={styles.passwordPlaceholder}>*</Text>
                <Text style={styles.passwordPlaceholder}>*</Text>
                <Text style={styles.passwordPlaceholder}>*</Text>
                <Text style={styles.passwordPlaceholder}>*</Text>
            </View>


            {/* 비밀번호 안내 텍스트 */}
            <View style={styles.passwordInfoContainer}>
                <Text style={styles.passwordChangeInfoText}>비밀번호를 변경해 주세요!</Text>
                <Text style={styles.passwordInfoText}>기존의 비밀번호와 새 비밀번호를 입력해주세요.</Text>
            </View>

            {/* 텍스트 입력란 */}
            <TextInput
                style={styles.inputBox}
                placeholder="현재 비밀번호"
                secureTextEntry={true} // 비밀번호 숨김 처리
            />
            <TextInput
                style={styles.inputBox}
                placeholder="새 비밀번호"
                secureTextEntry={true} // 비밀번호 숨김 처리
            />
            <TextInput
                style={styles.inputBox}
                placeholder="비밀번호 확인"
                secureTextEntry={true} // 비밀번호 숨김 처리
            />

            {/* 변경 완료 버튼 */}
            <TouchableOpacity style={styles.changePasswordButton}>
                <Text style={styles.changePasswordButtonText}>변경 완료</Text>
            </TouchableOpacity>
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
    iconContainer: {
        alignItems: 'center',
        marginTop: 50,
    },
    lockIcon: {
        width: 100,
        height: 100,
        marginBottom: 8,
    },
    passwordInput: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center', // 가로 중앙 정렬
        marginBottom: 20,
    },
    passwordPlaceholder: {
        fontSize: 24,
        borderBottomWidth: 1,
        width: 20, // 각 * 텍스트 너비 설정
        textAlign: 'center',
        fontWeight: 'bold',
        marginLeft: 10, // 각 * 텍스트 사이 간격 조절
        marginTop: 10,
        marginBottom: 30,
    },
    passwordChangeText: {
        fontSize: 24,
        flex: 1, // 텍스트가 남은 공간을 모두 차지하도록 설정
        textAlign: 'center',
    },
    passwordInfoContainer: {
        alignItems: 'center',
        marginBottom: 40,
    },
    passwordChangeInfoText: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    passwordInfoText: {
        fontSize: 14,
        color: '#7B7B7B',
    },
    inputBox: {
        borderWidth: 1,
        backgroundColor: '#F5F3F3',
        borderRadius: 10,
        padding: 12,
        marginBottom: 16,
        fontSize: 16,
        width: '90%',
        alignSelf: 'center',
    },
    changePasswordButton: {
        backgroundColor: '#FF965C', // 배경색
        borderRadius: 8, // BorderRadius 설정
        alignItems: 'center',
        justifyContent: 'center',
        height: 50, // 버튼 높이 조절
        marginTop: 16, // 버튼을 아래로 내립니다.
        width: '90%',
        alignSelf: 'center',
    },
    changePasswordButtonText: {
        color: 'white', // 텍스트 색상
        fontWeight: 'bold', // 텍스트를 bold체로 설정
        fontSize: 18
    },
});
