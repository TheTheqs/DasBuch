interface ProfileLineProps {
    label: string;
    content : string;
}
function ProfileLine({label, content}: ProfileLineProps) {
    return (
        <p style={{ margin: 0, fontSize: "1.2rem" }}>{label}: {content}</p>
    );
}

export default ProfileLine;